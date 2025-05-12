package minigame.minecraftFlagDuel.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import minigame.minecraftFlagDuel.constants.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class ShuffleTeam {
    private static Scoreboard teamBoard = Bukkit.getScoreboardManager().getMainScoreboard();

    public static int shuffleTeam(CommandContext<CommandSourceStack> ctx) {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);
        Collections.shuffle(Arrays.asList(Constants.teams));

        clearExistingTeams();

        for (int i = 1; i <= 10; ++i) {
            Team team = teamBoard.registerNewTeam(Constants.teams[i].teamName);
            team.color(Constants.teams[i].teamColor);
        }

        List<Team> teams = new ArrayList<>(teamBoard.getTeams());
        int teamCount = Math.min(onlinePlayers.size(), teams.size());
        for (int i = 0; i < onlinePlayers.size(); ++i) {
            Player player = onlinePlayers.get(i);
            Team team = teams.get(i % teamCount);
            team.addEntry(player.getName());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static void clearExistingTeams() {
        teamBoard.getTeams().forEach(Team::unregister);
    }
}