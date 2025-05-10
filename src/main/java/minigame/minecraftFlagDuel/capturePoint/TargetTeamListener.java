package minigame.minecraftFlagDuel.capturePoint;

import minigame.minecraftFlagDuel.capturePoint.entities.Target;
import minigame.minecraftFlagDuel.constants.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static minigame.minecraftFlagDuel.capturePoint.entities.Target.targetPoints;

// 对于每个占领点监听点内的队伍
public class TargetTeamListener {
    public static void checkAndNotifyTeam(int radius) {
        for (String targetName : targetPoints.keySet()) {
            Target target = targetPoints.get(targetName);

            Set<Team> teamsFound = new HashSet<>();
            Set<Player> playersInArea = new HashSet<>();

            for (Player player : target.location.getNearbyPlayers(radius)) {
                Team playerTeam = player.getScoreboard().getPlayerTeam(player);
                if (playerTeam != null) {
                    teamsFound.add(playerTeam);
                }
                playersInArea.add(player);
            }

            // 如果只找到一个队伍或没有队伍
            if (teamsFound.size() == 1) {
                Team team = teamsFound.iterator().next();
                if (target.currentTeam == null || !Objects.equals(team, target.currentTeam)) {
                    target.captureTick = 0;
                    target.currentTeam = team;
                }
                else {
                    target.captureTick++;
                    if (target.captureTick == Constants.captureTickNeed) {
                        target.captureTick = 0;

                        for (Player player : playersInArea) {
                            player.getInventory().addItem(target.resource);
                        }
                    }
                }
                String message = "正在占领：" + targetName + " - " + Integer.toString(target.captureTick);
                for (Player player : playersInArea) {
                    if (player.getScoreboard().getPlayerTeam(player) != null &&
                            Objects.equals(player.getScoreboard().getPlayerTeam(player), team)) {
                        player.sendActionBar(Component.text(message));
                    }
                }
            }
            else if (teamsFound.size() > 1) {
                String conflictMessage = "你的队伍正在争夺：" + targetName + "!";
                for (Player player : playersInArea) {
                    if (player.getScoreboard().getPlayerTeam(player) != null) {
                        player.sendActionBar(Component.text(conflictMessage));
                    }
                }
            }
            else {
                target.captureTick = 0;
                target.currentTeam = null;
            }
        }
    }
}
