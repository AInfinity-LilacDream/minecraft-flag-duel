package minigame.minecraftFlagDuel.capturePoint.entities;

import minigame.minecraftFlagDuel.constants.Constants;
import minigame.minecraftFlagDuel.miniDuel.MiniDuelGameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.beans.JavaBean;
import java.util.Objects;

public class ThrowableBlockItem implements Listener {

    public ThrowableBlockItem(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @EventHandler
            public void onProjectileHit(ProjectileHitEvent event) {
                if (event.getEntity() instanceof WindCharge windcharge) {
                    if (windcharge.getShooter() instanceof Player thrower && event.getHitEntity() instanceof Player hitplayer) {
                        int mode = Integer.parseInt(windcharge.getScoreboardTags().iterator().next());
                        MiniDuelGameManager newDuel = new MiniDuelGameManager(plugin);
                        newDuel.startGame(hitplayer, thrower, mode, 60);

                        event.setCancelled(true);
                    }
                }
            }
        }, Objects.requireNonNull(plugin));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            for (ShopItem duelItem : ShopItem.duelItems) {
                if (!itemInHand.getType().equals(Material.AIR) && itemInHand.isSimilar(duelItem.getItem())) {
                    event.setCancelled(true);

                    int selectedMode = duelItem.getMode();

                    if (itemInHand.getAmount() > 1) {
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    } else {
                        player.getInventory().setItemInMainHand(null);
                    }

                    launchThrowableBlock(player, selectedMode);
                }
            }
        }
    }

    private void launchThrowableBlock(Player player, int mode) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection().multiply(1.5); // ???

        WindCharge windcharge = player.getWorld().spawn(eyeLocation, WindCharge.class);
        windcharge.setShooter(player);
        windcharge.setVelocity(direction);
        windcharge.setInvulnerable(true);
        windcharge.setGravity(false);

        if (mode == Constants.RANDOM_ID) windcharge.addScoreboardTag(String.valueOf((int) (Math.random() * 3)));
        windcharge.addScoreboardTag(String.valueOf(mode));

        windcharge.getUniqueId();

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 20 * 10) {
                    windcharge.remove();
                    this.cancel();
                }
                else {
                    ticks++;
                }
            }
        }.runTaskTimer(Objects.requireNonNull(player.getServer().getPluginManager().getPlugin(Constants.pluginName)), 1, 1);
    }
}
