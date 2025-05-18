package minigame.minecraftFlagDuel.capturePoint.entities;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class InventorySwitcher {

    // 存储玩家原始物品栏数据
    private final Map<UUID, PlayerInventoryData> originalInventories = new HashMap<>();

    /**
     * 内部类用于保存玩家的所有装备信息。
     */
    private static class PlayerInventoryData {
        public ItemStack[] contents;
        public ItemStack helmet;
        public ItemStack chestplate;
        public ItemStack leggings;
        public ItemStack boots;
        public ItemStack offHand;
    }

    /**
     * 将玩家的物品栏替换为一个临时物品栏。
     *
     * @param player 玩家对象
     * @param tempInventory 主物品栏内容（36 格）
     * @param helmet 头盔
     * @param chestplate 胸甲
     * @param leggings 腿甲
     * @param boots 靴子
     * @param offHand 副手物品
     */
    public void switchToTemporaryInventory(
            Player player,
            Inventory tempInventory,
            ItemStack helmet,
            ItemStack chestplate,
            ItemStack leggings,
            ItemStack boots,
            ItemStack offHand) {

        UUID uuid = player.getUniqueId();

        if (!originalInventories.containsKey(uuid)) {
            PlayerInventoryData data = new PlayerInventoryData();
            data.contents = player.getInventory().getContents();
            data.helmet = player.getInventory().getHelmet();
            data.chestplate = player.getInventory().getChestplate();
            data.leggings = player.getInventory().getLeggings();
            data.boots = player.getInventory().getBoots();
            data.offHand = player.getInventory().getItemInOffHand();
            originalInventories.put(uuid, data);
        }

        // 设置主物品栏内容
        player.getInventory().setContents(tempInventory.getContents());

        // 设置盔甲
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);

        // 设置副手
        player.getInventory().setItemInOffHand(offHand);
    }

    /**
     * 恢复玩家的原始物品栏。
     *
     * @param player 玩家对象
     */
    public void restoreOriginalInventory(Player player) {
        UUID uuid = player.getUniqueId();

        if (originalInventories.containsKey(uuid)) {
            PlayerInventoryData data = originalInventories.get(uuid);

            player.getInventory().setContents(data.contents);
            player.getInventory().setHelmet(data.helmet);
            player.getInventory().setChestplate(data.chestplate);
            player.getInventory().setLeggings(data.leggings);
            player.getInventory().setBoots(data.boots);
            player.getInventory().setItemInOffHand(data.offHand);

            originalInventories.remove(uuid);
        }
    }

    /**
     * 创建一个测试用的临时物品栏（36 格）并提供固定装备
     */
    public Inventory createSwordInventory() {
        Inventory tempInventory = getServer().createInventory(null, 36, Component.text("sword"));

        // 主物品栏+快捷栏（0~35）
        for (int i = 0; i < 36; i++) {
            tempInventory.setItem(i, new ItemStack(Material.AIR));
        }

        tempInventory.setItem(0, new ItemStack(Material.IRON_SWORD));

        return tempInventory;
    }

    public Inventory createKnockBackInventory() {
        Inventory tempInventory = getServer().createInventory(null, 36, Component.text("knockback"));

        for (int i = 0; i < 36; i++) {
            tempInventory.setItem(i, new ItemStack(Material.AIR));
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();

        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);

        // 设置显示名称
        meta.displayName(Component.text("knockback stick"));

        stick.setItemMeta(meta);

        tempInventory.setItem(0, stick);
        return tempInventory;
    }

    public Inventory createPVEInventory() {
        Inventory tempInventory = getServer().createInventory(null, 36, Component.text("pve"));

        // 主物品栏+快捷栏（0~35）
        for (int i = 0; i < 36; i++) {
            tempInventory.setItem(i, new ItemStack(Material.AIR));
        }

        tempInventory.setItem(0, new ItemStack(Material.DIAMOND_SWORD));

        return tempInventory;
    }

    public Inventory createEmptyInventory() {
        Inventory tempInventory = getServer().createInventory(null, 36, Component.text("empty"));

        // 主物品栏+快捷栏（0~35）
        for (int i = 0; i < 36; i++) {
            tempInventory.setItem(i, new ItemStack(Material.AIR));
        }

        return tempInventory;
    }

    /**
     * 获取默认的测试盔甲和副手物品
     */
    public ItemStack[] getSwordArmorAndOffhand() {
        return new ItemStack[]{
                new ItemStack(Material.DIAMOND_HELMET),      // 头盔
                new ItemStack(Material.DIAMOND_CHESTPLATE),    // 胸甲
                new ItemStack(Material.DIAMOND_LEGGINGS),      // 腿甲
                new ItemStack(Material.DIAMOND_BOOTS),         // 靴子
                null
        };
    }
}