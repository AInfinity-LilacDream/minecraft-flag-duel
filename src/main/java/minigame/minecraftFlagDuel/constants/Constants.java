package minigame.minecraftFlagDuel.constants;

import minigame.minecraftFlagDuel.miniDuel.CuboidRegion;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Constants {
    public static final String worldName = "world";
    public static final String pluginName = "minecraftFlagDuel";

    public static final int captureTickNeed = 1 * 20;
    public static final int MAX_GAME_TOT = 20;
    public static final int GAME_CNT = 6;

    public static class TeamWithDetails {
        public String teamName;
        public NamedTextColor teamColor;

        public TeamWithDetails(String teamName, NamedTextColor teamColor) {
            this.teamName = teamName;
            this.teamColor = teamColor;
        }
    }

    public static Location[] duelShopVillagerLocation = new Location[]{
            new Location(Bukkit.getWorld(Constants.worldName), 141.5, 157, -94.5),
            new Location(Bukkit.getWorld(Constants.worldName), 141.5, 157, -111.5),
            new Location(Bukkit.getWorld(Constants.worldName), 141.5, 157, -128.5),
            new Location(Bukkit.getWorld(Constants.worldName), 43.5, 157, -133.5),
            new Location(Bukkit.getWorld(Constants.worldName), 43.5, 157, -116.5),
            new Location(Bukkit.getWorld(Constants.worldName), 43.5, 157, -99.5),
    };

    public static Location[] buffShopVillagerLocation = new Location[]{
            new Location(Bukkit.getWorld(Constants.worldName), 146.5, 157, -94.5),
            new Location(Bukkit.getWorld(Constants.worldName), 146.5, 157, -111.5),
            new Location(Bukkit.getWorld(Constants.worldName), 146.5, 157, -128.5),
            new Location(Bukkit.getWorld(Constants.worldName), 38.5, 157, -133.5),
            new Location(Bukkit.getWorld(Constants.worldName), 38.5, 157, -116.5),
            new Location(Bukkit.getWorld(Constants.worldName), 38.5, 157, -99.5),
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

    public static final Location[][] duelLocations = new Location[][] {
            {new Location(Bukkit.getWorld(Constants.worldName), 155.5, 160, 3.5),
                    new Location(Bukkit.getWorld(Constants.worldName), 155.5, 160, -4.5)},
            {new Location(Bukkit.getWorld(Constants.worldName), 155.5, 160, -25.5),
                    new Location(Bukkit.getWorld(Constants.worldName), 155.5, 160, -33.5)},
            {new Location(Bukkit.getWorld(Constants.worldName), 155.5, 160, 32.5),
                    new Location(Bukkit.getWorld(Constants.worldName), 155.5, 160, 24.5)},
            {new Location(Bukkit.getWorld(Constants.worldName), 153.5, 161, 57.5),
                    new Location(Bukkit.getWorld(Constants.worldName), 145.5, 161, 57.5)},
            {new Location(Bukkit.getWorld(Constants.worldName), 155.5, 161, 93.5),
                    new Location(Bukkit.getWorld(Constants.worldName), 142.5, 161, 80.5)},
            {new Location(Bukkit.getWorld(Constants.worldName), 155.5, 161, 123),
                    new Location(Bukkit.getWorld(Constants.worldName), 155.5, 161, 113)},
    };

    public static final int[] duelXOffsets = new int[] {-51, -51, -51, -30, -30, -66};

    public static final CuboidRegion[] duelWinRegion = new CuboidRegion[] {
            new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName), 122, 163, 5), new Location(Bukkit.getWorld(Constants.worldName), 120, 163, -6)),
            new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName), 122, 163, -24), new Location(Bukkit.getWorld(Constants.worldName), 120, 163, -35)),
            new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName), 122, 163, 34), new Location(Bukkit.getWorld(Constants.worldName), 120, 163, 23)),
            new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName), 0, 0, 0), new Location(Bukkit.getWorld(Constants.worldName), 0, 0, 0)),
            new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName), 0, 0, 0), new Location(Bukkit.getWorld(Constants.worldName), 0, 0, 0)),
            new CuboidRegion(new Location(Bukkit.getWorld(Constants.worldName), 104, 161, 125), new Location(Bukkit.getWorld(Constants.worldName), 100, 161, 111)),
    };

    public static final Location[] pveEntityLocation = new Location[] {
            new Location(Bukkit.getWorld(Constants.worldName), 141, 163, 125),
            new Location(Bukkit.getWorld(Constants.worldName), 141, 163, 121),
            new Location(Bukkit.getWorld(Constants.worldName), 134.5, 161, 119.5),
            new Location(Bukkit.getWorld(Constants.worldName), 134.5, 161, 125.5),
            new Location(Bukkit.getWorld(Constants.worldName), 134, 163.5, 123),
            new Location(Bukkit.getWorld(Constants.worldName), 124, 161, 125),
            new Location(Bukkit.getWorld(Constants.worldName), 124, 161, 121),
            new Location(Bukkit.getWorld(Constants.worldName), 116, 161, 125),
            new Location(Bukkit.getWorld(Constants.worldName), 116, 161, 121),
            new Location(Bukkit.getWorld(Constants.worldName), 108, 161, 125),
            new Location(Bukkit.getWorld(Constants.worldName), 108, 161, 121),
    };
    public static final int pvezOffset = -10;

    public static final int PARKOUR_EASY_ID =   0;
    public static final int PARKOUR_MEDIUM_ID = 1;
    public static final int PARKOUR_HARD_ID =   2;
    public static final int KNOCKBACK_ID =      3;
    public static final int SWORD_ID =          4;
    public static final int PVE_ID =            5;
    public static final int RANDOM_ID =         6;
}
