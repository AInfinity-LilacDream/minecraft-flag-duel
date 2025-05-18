package minigame.minecraftFlagDuel.capturePoint.entities;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    // 存储每个玩家的BossBar实例和独立的生命值
    private static final Map<UUID, BossBarData> bossBarDataMap = new HashMap<>();

    private static class BossBarData {
        public BossBar bossBar;
        public int customHealth;

        public BossBarData(BossBar bossBar, int customHealth) {
            this.bossBar = bossBar;
            this.customHealth = customHealth;
        }
    }

    public static void createOrUpdateBossBar(Player player) {
        UUID playerId = player.getUniqueId();

        if (!bossBarDataMap.containsKey(playerId)) {
            // 创建一个新的BossBar
            BossBar bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
            bossBar.setVisible(true);
            bossBar.addPlayer(player);

            // 初始化自定义生命值为5
            BossBarData bossBarData = new BossBarData(bossBar, 5);
            bossBarDataMap.put(playerId, bossBarData);

            // 更新BossBar内容
            updateBossBar(bossBarData);
        }
        // 如果已经有BossBar，则不做任何操作
    }

    private static void updateBossBar(BossBarData bossBarData) {
        BossBar bossBar = bossBarData.bossBar;
        int customHealth = bossBarData.customHealth;
        int maxHealth = 5; // 最大生命值

        // 设置BossBar的消息和进度
        bossBar.setTitle("剩余生命数: " + customHealth + "/" + maxHealth);
        bossBar.setProgress((double) customHealth / maxHealth);

        // 可选：根据剩余生命数改变颜色或样式
        if (customHealth <= 1) {
            bossBar.setColor(BarColor.RED);
        } else if (customHealth <= 3) {
            bossBar.setColor(BarColor.YELLOW);
        } else {
            bossBar.setColor(BarColor.GREEN);
        }
    }

    public static void reduceCustomHealth(Player player, int amount) {
        UUID playerId = player.getUniqueId();
        BossBarData bossBarData = bossBarDataMap.get(playerId);

        if (bossBarData != null) {
            // 减少生命值
            bossBarData.customHealth -= amount;
            // 确保生命值不低于0
            bossBarData.customHealth = Math.max(bossBarData.customHealth, 0);

            // 更新BossBar
            updateBossBar(bossBarData);
        }
    }

    public static void setCustomHealth(Player player, int newHealth) {
        UUID playerId = player.getUniqueId();
        BossBarData bossBarData = bossBarDataMap.get(playerId);

        if (bossBarData != null) {
            // 设置新的生命值
            bossBarData.customHealth = Math.min(Math.max(newHealth, 0), 5); // 确保生命值在0到5之间
            // 更新BossBar
            updateBossBar(bossBarData);
        }
    }

    public static BossBarData getBossBarData(Player player) {
        return bossBarDataMap.get(player.getUniqueId());
    }
}