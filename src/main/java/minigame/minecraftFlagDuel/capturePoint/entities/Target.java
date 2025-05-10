package minigame.minecraftFlagDuel.capturePoint.entities;


import minigame.minecraftFlagDuel.constants.Constants;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class Target {
    public Location location;
    public Particle particle;
    public Particle.DustOptions dustOption;
    public ItemStack resource;
    public ItemMeta resourceMeta;

    public int captureTick = 0;
    public Team currentTeam = null;

    public Target(Location location, Particle particle, Particle.DustOptions dustOption,
                  ItemStack resource, String resourceName) {
        this.location = location;
        this.particle = particle;
        this.dustOption = dustOption;
        this.resource = resource;

        this.resourceMeta = this.resource.getItemMeta();
        resourceMeta.displayName(net.kyori.adventure.text.Component.text(resourceName,
                    net.kyori.adventure.text.format.NamedTextColor.GOLD));
        this.resource.setItemMeta(this.resourceMeta);
    }

    // green
    private static final Target targetKnockback = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 74.5, 157, -105.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.GREEN, 1.0F),
            new ItemStack(Material.GREEN_GLAZED_TERRACOTTA, 1),
            "Knockback duel resource"
    );

    // lime
    private static final Target targetPVE = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 74.5, 157, -122.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.LIME, 1.0F),
            new ItemStack(Material.LIME_GLAZED_TERRACOTTA, 1),
            "PVE duel resource"
    );

    // orange
    private static final Target targetParkour = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 110.5, 157, -122.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.ORANGE, 1.0F),
            new ItemStack(Material.ORANGE_GLAZED_TERRACOTTA, 1),
            "Parkour duel resource"
    );

    // red
    private static final Target targetSword = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 110.5, 157, -105.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.RED, 1.0F),
            new ItemStack(Material.RED_GLAZED_TERRACOTTA),
            "Sword duel resource"
    );

    // cyan
    private static final Target targetProtection1 = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 91.5, 157, -96.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.AQUA, 1.0F),
            new ItemStack(Material.CYAN_GLAZED_TERRACOTTA, 1),
            "Protection duel resource"
    );
    private static final Target targetProtection2 = new Target(
            new Location(org.bukkit.Bukkit.getWorld(Constants.worldName), 91.5, 157, -131.5),
            Particle.SMOKE,
            new Particle.DustOptions(Color.AQUA, 1.0F),
            new ItemStack(Material.CYAN_GLAZED_TERRACOTTA, 1),
            "Protection duel resource"
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