package minigame.minecraftFlagDuel.constants;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String worldName = "world";
    public static final String pluginName = "minecraftFlagDuel";

    public static final int captureTickNeed = 1 * 20;

    public static class TeamWithDetails {
        public String teamName;
        public NamedTextColor teamColor;

        public TeamWithDetails(String teamName, NamedTextColor teamColor) {
            this.teamName = teamName;
            this.teamColor = teamColor;
        }
    }

    public static Location[] duelShopVillagerLocation = new Location[]{
            new Location(Bukkit.getWorld(Constants.worldName), 141, 157, -95),
            new Location(Bukkit.getWorld(Constants.worldName), 141, 157, -112),
            new Location(Bukkit.getWorld(Constants.worldName), 141, 157, -129),
            new Location(Bukkit.getWorld(Constants.worldName), 43, 157, -134),
            new Location(Bukkit.getWorld(Constants.worldName), 141, 157, -117),
            new Location(Bukkit.getWorld(Constants.worldName), 141, 157, -100),
    };

    public static Location[] buffShopVillagerLocation = new Location[]{
            new Location(Bukkit.getWorld(Constants.worldName), 146, 157, -95),
            new Location(Bukkit.getWorld(Constants.worldName), 146, 157, -112),
            new Location(Bukkit.getWorld(Constants.worldName), 146, 157, -129),
            new Location(Bukkit.getWorld(Constants.worldName), 38, 157, -134),
            new Location(Bukkit.getWorld(Constants.worldName), 38, 157, -117),
            new Location(Bukkit.getWorld(Constants.worldName), 38, 157, -100),
    };

    public static final TeamWithDetails[] teams = {
            new TeamWithDetails("红", NamedTextColor.RED),
            new TeamWithDetails("橙", NamedTextColor.GOLD),
            new TeamWithDetails("黄", NamedTextColor.YELLOW),
            new TeamWithDetails("黄绿", NamedTextColor.GREEN),
            new TeamWithDetails("绿", NamedTextColor.DARK_GREEN),
            new TeamWithDetails("青", NamedTextColor.AQUA),
            new TeamWithDetails("淡蓝", NamedTextColor.BLUE),
            new TeamWithDetails("蓝", NamedTextColor.DARK_BLUE),
            new TeamWithDetails("紫", NamedTextColor.DARK_PURPLE),
            new TeamWithDetails("品红", NamedTextColor.LIGHT_PURPLE),
    };
}
