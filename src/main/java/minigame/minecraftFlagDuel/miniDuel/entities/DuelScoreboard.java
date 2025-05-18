package minigame.minecraftFlagDuel.miniDuel.entities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.*;

public class DuelScoreboard {
    private final Scoreboard scoreboard;
    private Objective objective;

    public DuelScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(
                "duel_score",
                Criteria.DUMMY,
                Component.text("决斗分数").color(NamedTextColor.GOLD),
                RenderType.INTEGER
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void hideAndDestroy() {
        // 1. 获取主计分板(默认计分板)
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // 2. 将所有使用此计分板的玩家重置回主计分板
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard().equals(this.scoreboard)) {
                player.setScoreboard(mainScoreboard);
            }
        }

        // 3. 注销计分板目标
        if (this.objective != null) {
            this.objective.unregister();
            this.objective = null; // 清除引用
        }

        // 4. 清除显示槽位
        this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        this.scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);
        this.scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
    }

    public void setScore(Player player, int score) {
        objective.getScore(player.getName()).setScore(score);
    }

    public void addScore(Player player, int points) {
        setScore(player, objective.getScore(player.getName()).getScore() + points);
    }

    public int getScore(Player player) {
        return objective.getScore(player.getName()).getScore();
    }

    public void showToPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }
}