package minigame.minecraftFlagDuel.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messager {
    public static void sendGlobalInfo(String message) {
        String infoStr = "<blue>[Info]</blue> ";
        message = infoStr + message;
        Component parsed = MiniMessage.miniMessage().deserialize(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(parsed);
        }
    }

    public static void sendInfoToPlayer(String message, Player player) {
        String infoStr = "<blue>[Info]</blue> ";
        message = infoStr + message;
        Component parsed = MiniMessage.miniMessage().deserialize(message);
        player.sendMessage(parsed);
    }
}
