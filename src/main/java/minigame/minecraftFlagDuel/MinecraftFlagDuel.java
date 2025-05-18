package minigame.minecraftFlagDuel;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import minigame.minecraftFlagDuel.capturePoint.DistanceListener;
import minigame.minecraftFlagDuel.capturePoint.TargetParticleEffect;
import minigame.minecraftFlagDuel.capturePoint.TargetTeamListener;
import minigame.minecraftFlagDuel.capturePoint.VillagerShopManager;
import minigame.minecraftFlagDuel.capturePoint.entities.BossBarManager;
import minigame.minecraftFlagDuel.capturePoint.entities.ThrowableBlockItem;
import minigame.minecraftFlagDuel.commands.ShuffleTeam;
import minigame.minecraftFlagDuel.constants.Constants;
import minigame.minecraftFlagDuel.miniDuel.MiniDuelGameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class MinecraftFlagDuel extends JavaPlugin {

    private final Map<UUID, UUID> lastAttackerMap = new HashMap<>();

    @Override
    public void onEnable() {
//        getServer().getPluginManager().registerEvents(new DistanceListener(), this);
//        new TargetParticleEffect(10).start();

        getServer().getWorld(Constants.worldName).setGameRule(org.bukkit.GameRule.NATURAL_REGENERATION, false);
        getServer().getWorld(Constants.worldName).setGameRule(GameRule.FALL_DAMAGE, false);
        getServer().getWorld(Constants.worldName).setGameRule(GameRule.DO_MOB_LOOT, false);

        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBarManager.createOrUpdateBossBar(player);
        }

        Bukkit.getWorlds().forEach(world ->
                world.getEntities().stream()
                        .filter(e -> e instanceof Villager)
                        .forEach(Entity::remove)
        );

        LiteralCommandNode<CommandSourceStack> shuffleTeams = Commands.literal("shuffleteam")
                .executes(ShuffleTeam::shuffleTeam).build();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(shuffleTeams);
        });

        this.getServer().getScheduler().runTaskTimer(this, () -> {
            TargetTeamListener.checkAndNotifyTeam(5.5);
        }, 0L, 1L); // 初始延迟0刻后开始，之后每隔20刻(即1秒)重复一次

        new VillagerShopManager(this);

        new ThrowableBlockItem(this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                // 设置饱食度为最大值20
                player.setFoodLevel(20);
                // 将饱和度设为0，这会阻止自然回血
                player.setSaturation(20F);
            }
        }, 0L, 20L); // 开始延时0刻后执行，之后每隔20刻(1秒)执行一次

        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent event) {
                Player player = event.getPlayer();

                boolean flag = false;
                for (int mode = 0; mode < Constants.GAME_CNT; ++mode) {
                    for (int i = 0; i < Constants.MAX_GAME_TOT; ++i) {
                        MiniDuelGameManager game = MiniDuelGameManager.gameInstances[mode][i];
                        if (game != null && game.getIsGameActive()) {
                            if (game.getPlayer1() == player || game.getPlayer2() == player) {
                                game.onPlayerMove(player);
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) break;
                }
                if (!flag && player.getGameMode() != GameMode.SPECTATOR) {
                    if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.CYAN_TERRACOTTA) {
                        player.setHealth(0.0);
                    }
                }
            }

            @EventHandler
            public void onCombust(EntityCombustEvent event) {
                if (event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.SKELETON) {
                    event.setCancelled(true); // 取消燃烧
                }
            }

            @EventHandler
            public void onPlayerDeath(PlayerDeathEvent event) {
                Player player = event.getEntity();

                boolean flag = false;
                for (int mode = 0; mode < Constants.GAME_CNT; ++mode) {
                    for (int i = 0; i < Constants.MAX_GAME_TOT; ++i) {
                        MiniDuelGameManager game = MiniDuelGameManager.gameInstances[mode][i];
                        if (game != null && game.getIsGameActive()) {
                            if (game.getPlayer1() == player || game.getPlayer2() == player) {
                                event.setCancelled(true);
                                game.onPlayerDeath(player);
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) break;
                }
                if (!flag && player.getGameMode() != GameMode.SPECTATOR) {
                    Player victim = event.getEntity();
                    UUID victimUuid = victim.getUniqueId();
                    UUID attackerUuid = lastAttackerMap.getOrDefault(victimUuid, null);

                    event.setCancelled(true);
                    event.setKeepInventory(false);
                    event.getDrops().clear();

                    if (attackerUuid != null) {
                        Player attacker = Bukkit.getPlayer(attackerUuid);
                        if (attacker != null && attacker.isOnline()) {
                            transferInventory(victim, attacker);
                        }
                    }

                    resetPlayerInventory(player);
                    player.setHealth(20.0);
                    player.teleport(new Location(player.getWorld(), 94, 158, -111));
                }
            }

            @EventHandler
            public void onEntityDamage(EntityDamageByEntityEvent event) {
                if (event.getEntity() instanceof Player player) {
                    if (event.getDamager() instanceof Player attacker) {
                        lastAttackerMap.put(player.getUniqueId(), attacker.getUniqueId());
                    }
                }
            }

            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                BossBarManager.createOrUpdateBossBar(player);
            }
        }, this);
    }

    private void resetPlayerInventory(Player player) {
        PlayerInventory inv = player.getInventory();

        // 清空背包和盔甲栏
        inv.clear();
        inv.setArmorContents(null);

        // 创建初始装备
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);

        // 添加到背包
        inv.addItem(stoneSword);

        // 设置盔甲
        inv.setHelmet(leatherHelmet);
        inv.setChestplate(leatherChestplate);
        inv.setLeggings(leatherLeggings);
        inv.setBoots(leatherBoots);
    }

    private void transferInventory(Player victim, Player attacker) {
        ItemStack[] contents = victim.getInventory().getContents();

        for (ItemStack item : contents) {
            if (shouldTransfer(item)) {
                // 尝试将物品放入攻击者背包
                Map<Integer, ItemStack> leftOver = attacker.getInventory().addItem(item);

                // 如果背包已满，丢在地上
                for (ItemStack remaining : leftOver.values()) {
                    attacker.getWorld().dropItemNaturally(attacker.getLocation(), remaining);
                }
            }
        }
    }

    private boolean shouldTransfer(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (item.getType() == Material.STONE_SWORD) return false;

        // 检查是否是盔甲
        Material itemType = item.getType();
        return !(itemType == Material.LEATHER_HELMET || itemType == Material.LEATHER_CHESTPLATE ||
                itemType == Material.LEATHER_LEGGINGS || itemType == Material.LEATHER_BOOTS);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
