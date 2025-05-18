package minigame.minecraftFlagDuel.miniDuel;

import minigame.minecraftFlagDuel.capturePoint.entities.BossBarManager;
import minigame.minecraftFlagDuel.capturePoint.entities.InventorySwitcher;
import minigame.minecraftFlagDuel.commands.Messager;
import minigame.minecraftFlagDuel.constants.Constants;
import minigame.minecraftFlagDuel.miniDuel.entities.DuelScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.attribute.Attribute;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MiniDuelGameManager implements Listener {
    public static MiniDuelGameManager[][] gameInstances = new MiniDuelGameManager[Constants.GAME_CNT][Constants.MAX_GAME_TOT];

    public int mode;

    private Player player1;
    private Player player2;
    private BukkitRunnable gameEndTask, timerTask;
    private Boolean isGameActive;
    private CuboidRegion winRegion;
    private int gameId;

    private int xoffset;
    private int playerFinishTot = 0;
    private boolean player1finished = false;
    private boolean player2finished = false;

    private DuelScoreboard duelScoreboard;

    private Location player1Location, player2Location;
    private final InventorySwitcher inventorySwitcher = new InventorySwitcher();

    private int remainingTime;
    private final JavaPlugin plugin;

    public MiniDuelGameManager(JavaPlugin plugin) {
        this.isGameActive = false;
        this.duelScoreboard = new DuelScoreboard();
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        // 检查：击杀者是否有效 + 实体是否有当前游戏的 tag
        if ((killer == player1 || killer == player2) && entity.getScoreboardTags().contains(Integer.toString(gameId))) {
            duelScoreboard.addScore(killer, 10);
        }
    }

    public void destroy() {
        // 1. 取消所有正在运行的任务
        if (gameEndTask != null) {
            gameEndTask.cancel();
        }

        // 2. 重置玩家状态
        if (player1 != null) {
            resetPlayer(player1);
        }
        if (player2 != null) {
            resetPlayer(player2);
        }

        // 3. 清理计分板
        if (duelScoreboard != null) {
            duelScoreboard.hideAndDestroy();
        }

        // 4. 清理生成的实体（PVE模式）
        if (mode == Constants.PVE_ID) {
            World world = Bukkit.getWorld(Constants.worldName);
            if (world != null) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getScoreboardTags().contains(Integer.toString(gameId))) {
                        entity.remove();
                    }
                }
            }
        }

        if (timerTask != null) {
            timerTask.cancel();
        }

        // 5. 从全局数组中移除引用
        if (gameInstances[mode][gameId] == this) {
            gameInstances[mode][gameId] = null;
        }

        // 6. 取消事件监听
        HandlerList.unregisterAll(this);
    }

    private void resetPlayer(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.setHealth(20);
        player.teleport(new Location(player.getWorld(), 94, 158, -111));
        inventorySwitcher.restoreOriginalInventory(player);
    }

    public void showScoreboardTo(Player player) {
        duelScoreboard.showToPlayer(player);
    }

    public void startGame(Player player1, Player player2, int mode, int gameTimeSeconds) {
        this.mode = mode;
        this.player1 = player1;
        this.player2 = player2;

        duelScoreboard.setScore(player1, 0);
        duelScoreboard.setScore(player2, 0);

        if (mode == Constants.PVE_ID) showScoreboardTo(player1);
        if (mode == Constants.PVE_ID) showScoreboardTo(player2);

        Messager.sendInfoToPlayer("<aqua>向</aqua><gold><bold>" + this.player1.getName() + "</bold></gold><aqua>发起了挑战！</aqua>", this.player2);
        Messager.sendInfoToPlayer("<aqua>被</aqua><gold><bold>" + this.player2.getName() + "</bold></gold><aqua>拉入了决斗！</aqua>", this.player1);

        ItemStack[] gear = inventorySwitcher.getSwordArmorAndOffhand();

        player1.setGameMode(GameMode.ADVENTURE);
        player2.setGameMode(GameMode.ADVENTURE);

        for (int i = 0; i < Constants.MAX_GAME_TOT; ++i) {
            if (gameInstances[mode][i] == null) {
                gameInstances[mode][i] = this;
                this.gameId = i;
                this.xoffset = i;
                this.player1Location = new Location(Bukkit.getWorld(Constants.worldName), Constants.duelLocations[mode][0].getX() + Constants.duelXOffsets[mode] * this.xoffset,
                        Constants.duelLocations[mode][0].getY(), Constants.duelLocations[mode][0].getZ());
                this.player2Location = new Location(Bukkit.getWorld(Constants.worldName), Constants.duelLocations[mode][1].getX() + Constants.duelXOffsets[mode] * this.xoffset,
                        Constants.duelLocations[mode][1].getY(), Constants.duelLocations[mode][1].getZ());

                this.winRegion = createWinRegionWithXOffset(Constants.duelWinRegion[mode], this.xoffset, this.mode);
                break;
            }
        }

        if (mode == Constants.SWORD_ID) {
            Inventory tempinv = inventorySwitcher.createSwordInventory();
            inventorySwitcher.switchToTemporaryInventory(player1, tempinv,
                    gear[0], gear[1], gear[2], gear[3], gear[4]);
            inventorySwitcher.switchToTemporaryInventory(player2, tempinv,
                    gear[0], gear[1], gear[2], gear[3], gear[4]);
        }
        else if (mode == Constants.KNOCKBACK_ID) {
            Inventory tempinv = inventorySwitcher.createKnockBackInventory();
            inventorySwitcher.switchToTemporaryInventory(player1, tempinv,
                    gear[0], gear[1], gear[2], gear[3], gear[4]);
            inventorySwitcher.switchToTemporaryInventory(player2, tempinv,
                    gear[0], gear[1], gear[2], gear[3], gear[4]);
        }
        else if (mode == Constants.PVE_ID) {
            Inventory tempinv = inventorySwitcher.createPVEInventory();
            inventorySwitcher.switchToTemporaryInventory(player1, tempinv,
                    gear[0], gear[1], gear[2], gear[3], gear[4]);
            inventorySwitcher.switchToTemporaryInventory(player2, tempinv,
                    gear[0], gear[1], gear[2], gear[3], gear[4]);
        }
        else if (mode <= 2) {
            Inventory tempinv = inventorySwitcher.createEmptyInventory();
            inventorySwitcher.switchToTemporaryInventory(player1, tempinv,
                    null, null, null, null, null);
            inventorySwitcher.switchToTemporaryInventory(player2, tempinv,
                    null, null, null, null, null);
        }

        if (!isGameActive) {
            isGameActive = true;
            player1.teleport(this.player1Location);
            player2.teleport(this.player2Location);

            startCountDown(() -> {
                startGameEndTimer(gameTimeSeconds);
            });
        }
    }

    private CuboidRegion createWinRegionWithXOffset(CuboidRegion winRegion, int xOffset, int mode) {
        return new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName),
                winRegion.getPoint1().getX() + Constants.duelXOffsets[mode] * xOffset,
                winRegion.getPoint1().getY(), winRegion.getPoint1().getZ()),
                new Location(Bukkit.getWorld(Constants.worldName),
                        winRegion.getPoint2().getX() + Constants.duelXOffsets[mode] * xOffset,
                        winRegion.getPoint2().getY(), winRegion.getPoint2().getZ()));
    }

    private void startCountDown(Runnable onFinish) {
        new BukkitRunnable() {
            int timeLeft = 3; // 3秒倒计时

            @Override
            public void run() {
                if (timeLeft > 0) {
                    // 根据剩余时间发送不同颜色的标题
                    Title title;
                    switch (timeLeft) {
                        case 3:
                            title = Title.title(
                                    Component.text("3").color(NamedTextColor.GREEN),
                                    Component.empty(),
                                    Title.Times.times(Ticks.duration(0), Ticks.duration(20), Ticks.duration(0)
                                    ));
                            break;
                        case 2:
                            title = Title.title(
                                    Component.text("2").color(NamedTextColor.YELLOW),
                                    Component.empty(),
                                    Title.Times.times(Ticks.duration(0), Ticks.duration(20), Ticks.duration(0)
                                    ));
                            break;
                        case 1:
                            title = Title.title(
                                    Component.text("1").color(NamedTextColor.RED),
                                    Component.empty(),
                                    Title.Times.times(Ticks.duration(0), Ticks.duration(20), Ticks.duration(0))
                            );
                            break;
                        default:
                            title = null;
                    }
                    if (title != null) {
                        player1.showTitle(title);
                        player2.showTitle(title);
                    }
                    timeLeft--;
                } else {
                    for (int i = 0; i < 11; ++i) {
                        Location loc = Constants.pveEntityLocation[i];
                        World world = Bukkit.getWorld(Constants.worldName);
                        Location locr = new Location(world, Constants.pveEntityLocation[i].getX(),
                                Constants.pveEntityLocation[i].getY(), Constants.pveEntityLocation[i].getZ() + Constants.pvezOffset);
                        if (i == 4) {
                            Skeleton skl = (Skeleton) world.spawnEntity(loc, EntityType.SKELETON);
                            skl.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
                            skl.addScoreboardTag(Integer.toString(gameId));
                            Skeleton skr = (Skeleton) world.spawnEntity(locr, EntityType.SKELETON);
                            skr.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
                            skr.addScoreboardTag(Integer.toString(gameId));
                        }
                        else {
                            Zombie zbl = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
                            zbl.addScoreboardTag(Integer.toString(gameId));
                            Zombie zbr = (Zombie) world.spawnEntity(locr, EntityType.ZOMBIE);
                            zbr.addScoreboardTag(Integer.toString(gameId));
                        }
                    }

                    // 倒计时结束显示"START"
                    Title startTitle = Title.title(
                            Component.text("START").color(NamedTextColor.GOLD),
                            Component.empty(),
                            Title.Times.times(Ticks.duration(0), Ticks.duration(20), Ticks.duration(10))
                    );
                    player1.showTitle(startTitle);
                    player2.showTitle(startTitle);
                    cancel();
                    onFinish.run();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(Constants.pluginName)), 0L, 20L);
    }

    private void startGameEndTimer(final int seconds) {
        remainingTime = seconds;
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingTime <= 0 || !isGameActive) {
                    cancel();
                    return;
                }

                // 更新玩家经验栏
                updateXpTimer(player1, remainingTime);
                updateXpTimer(player2, remainingTime);

                remainingTime--;
            }
        };
        timerTask.runTaskTimer(this.plugin, 0L, 20L); // 每秒更新一次
        gameEndTask = new BukkitRunnable() {
            public void run() {
                endGame(null);
            }
        };
        gameEndTask.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(Constants.pluginName)), seconds * 20L);
    }

    private void updateXpTimer(Player player, int secondsLeft) {

        // 设置经验等级为剩余分钟数
        player.setLevel(secondsLeft);

        // 设置经验进度为剩余秒数的百分比（0.0~1.0）
        float progress = (float) secondsLeft / 60f;
        player.setExp(progress);
    }

    public void playerToFinish(Player player) {
        if ((player == player1 && player1finished) ||
                (player == player2 && player2finished)) return;
        announce("<aqua>" + player.getName() + "到达了终点！</aqua>");
        player.setGameMode(GameMode.SPECTATOR);
        if (player == player1) {
            player1finished = true;
            playerFinishTot++;
        }
        else {
            player2finished = true;
            playerFinishTot++;
        }
        if (playerFinishTot == 2) {
            int p1s = duelScoreboard.getScore(player1);
            int p2s = duelScoreboard.getScore(player2);
            if (p1s == p2s) endGame(null);
            else if (p1s > p2s) endGame(player1);
            else endGame(player2);
        }
    }

    public void playerCompletedGame(Player player) {
        if (isGameActive) {
            endGame(player);
        }
    }

    private void endGame(Player winner) {
        player1.setGameMode(GameMode.SPECTATOR);
        player2.setGameMode(GameMode.SPECTATOR);

        if (this.mode == Constants.PVE_ID) {
            World world = Bukkit.getWorld(Constants.worldName);
            for (Entity entity : world.getEntities()) {
                // 检查实体是否为僵尸或骷髅并且包含指定的标签
                if ((entity instanceof Zombie || entity instanceof Skeleton) && entity.getScoreboardTags().contains(Integer.toString(this.gameId))) {
                    // 移除实体
                    entity.remove();
                }
            }
        }

        isGameActive = false;
        if (gameEndTask != null) {
            gameEndTask.cancel();
        }

        this.winRegion = null;
        gameInstances[mode][this.gameId] = null;

        if (mode != Constants.PVE_ID || winner != null) announce(winner == null ? "<red>时间耗尽！</red>" : "<gold><bold>" + winner.getName() + "</bold></gold><aqua>赢得了决斗！</aqua>");
        else {
            if (duelScoreboard.getScore(player1) > duelScoreboard.getScore(player2)) {
                announce("<gold><bold>" + player1.getName() + "</bold></gold><aqua>赢得了决斗！</aqua>");
            }
            else if (duelScoreboard.getScore(player2) > duelScoreboard.getScore(player1)) {
                announce("<gold><bold>" + player2.getName() + "</bold></gold><aqua>赢得了决斗！</aqua>");
            }
            else announce("<aqua>双方平局！</aqua>");
        }

        if (winner == null) {
            BossBarManager.reduceCustomHealth(player1, 1);
            BossBarManager.reduceCustomHealth(player2, 1);
        }
        else if (winner == player1) BossBarManager.reduceCustomHealth(player2, 2);
        else if (winner == player2) BossBarManager.reduceCustomHealth(player1, 2);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(Constants.pluginName)), () -> {
            player1.setHealth(20);
            player1.teleport(new Location(player1.getWorld(), 94, 158, -111));
            player1.setGameMode(GameMode.CREATIVE);
            player2.setHealth(20);
            player2.teleport(new Location(player2.getWorld(), 94, 158, -111));
            player2.setGameMode(GameMode.CREATIVE);
            inventorySwitcher.restoreOriginalInventory(player1);
            inventorySwitcher.restoreOriginalInventory(player2);
            duelScoreboard.hideAndDestroy();
            this.destroy();
        }, 20L * 2);

    }

    public void onPlayerMove(Player player) {
        if (!isGameActive || winRegion == null) return;

        if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.CYAN_TERRACOTTA) {
            if (mode <= 2) {
                if (player == player1) player.teleport(player1Location);
                else player.teleport(player2Location);
            }
            else {
                playerCompletedGame(player == player1 ? player2 : player1);
            }
            return;
        }

        if (winRegion.contains(player.getLocation())) {
            if (this.mode == Constants.PVE_ID) {
                if (player.getGameMode() != GameMode.SPECTATOR) {
                    duelScoreboard.addScore(player, 50);
                    playerToFinish(player);
                }
            }
            else playerCompletedGame(player);
        }
    }

    public void onPlayerDeath(Player player) {
        if (!isGameActive) return;
        player.setGameMode(GameMode.SPECTATOR);
        playerCompletedGame(player == player1 ? player2 : player1);
    }

    private void announce(String message) {
        Messager.sendInfoToPlayer(message, player1);
        Messager.sendInfoToPlayer(message, player2);
    }

    public Boolean getIsGameActive() {
        return isGameActive;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
