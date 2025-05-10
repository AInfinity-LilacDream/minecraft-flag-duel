package minigame.minecraftFlagDuel.constants;

import net.kyori.adventure.text.format.NamedTextColor;

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

    public static final TeamWithDetails[] teams = {
            new TeamWithDetails("dummy", NamedTextColor.WHITE),
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
