/*
DistanceListener: Check whether player is in a target point. if so, accumulate time
 */

package minigame.minecraftFlagDuel.capturePoint;

import minigame.minecraftFlagDuel.capturePoint.entities.Target;
import minigame.minecraftFlagDuel.constants.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static minigame.minecraftFlagDuel.capturePoint.entities.Target.targetPoints;

public class DistanceListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        double minDistance = Double.MAX_VALUE;
        String closest = null;

        for (String targetName : targetPoints.keySet()) {
            Target targetLocation = targetPoints.get(targetName);
            double distance = playerLocation.distance(targetLocation.location);
            if (distance < minDistance) {
                minDistance = distance;
                closest = targetName;
            }
        }

        if (closest != null) {
            player.sendActionBar(Component.text(closest));
        }
    }
}
