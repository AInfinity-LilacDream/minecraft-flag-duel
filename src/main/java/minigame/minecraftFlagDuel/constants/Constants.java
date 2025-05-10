package minigame.minecraftFlagDuel.constants;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String worldName = "world";
    public static final String pluginName = "minecraftFlagDuel";

    public static class Target {
        public Location location;
        public Particle particle;
        public Particle.DustOptions dustOption;

        public Target(Location location, Particle particle, Particle.DustOptions dustOption) {
            this.location = location;
            this.particle = particle;
            this.dustOption = dustOption;
        }
    }

    // green
    private static final Target targetKnockback = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 74.5, 157, -105.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.GREEN, 1.0F)
    );

    // lime
    private static final Target targetPVE = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 74.5, 157, -122.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.LIME, 1.0F)
    );

    // orange
    private static final Target targetParkour = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 110.5, 157, -122.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.ORANGE, 1.0F)
    );

    // red
    private static final Target targetSword = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 110.5, 157, -105.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.RED, 1.0F)
    );

    // cyan
    private static final Target targetProtection1 = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 91.5, 157, -96.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.AQUA, 1.0F)
    );
    private static final Target targetProtection2 = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 91.5, 157, -131.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.AQUA, 1.0F)
    );

    public static final Map<String, Target> targetPoints = new HashMap<String, Target>() {{
        put("Point Knockback", targetKnockback);
        put("Point PVE", targetPVE);
        put("Point Parkour", targetParkour);
        put("Point Sword", targetSword);
        put("Point Protection1", targetProtection1);
        put("Point Protection2", targetProtection2);
    }};
}
