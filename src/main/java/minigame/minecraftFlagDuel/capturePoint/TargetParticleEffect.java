package minigame.minecraftFlagDuel.capturePoint;

import minigame.minecraftFlagDuel.constants.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static minigame.minecraftFlagDuel.constants.Constants.targetPoints;

public class TargetParticleEffect {
    private final int intervalTicks;

    public TargetParticleEffect(int intervalTicks) {
        this.intervalTicks = intervalTicks;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Constants.Target center : targetPoints.values()) {
                    drawCircle(center, 6.0, 50); // 半径3格，用20个粒子点组成圆圈
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(Constants.pluginName)), 0L, intervalTicks);
    }

    private void drawCircle(Constants.Target center, double radius, int particles) {
        Location loc = center.location;

        World world = loc.getWorld();
        if (world == null) return;

        double increment = (2 * Math.PI) / particles;
        for (int i = 0; i < particles; i++) {
            double angle = i * increment;
            double x = loc.getX() + (radius * Math.cos(angle));
            double z = loc.getZ() + (radius * Math.sin(angle));
            double y = loc.getY();

            Location currentLocation = new Location(world, x, y, z);

            // 发送粒子
            world.spawnParticle(Particle.DUST, currentLocation, 50, center.dustOption);
        }
    }
}
