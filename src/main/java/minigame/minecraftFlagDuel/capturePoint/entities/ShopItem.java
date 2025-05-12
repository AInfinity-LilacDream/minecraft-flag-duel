package minigame.minecraftFlagDuel.capturePoint.entities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopItem {
    private final ItemStack item;
    private final ItemMeta meta;
    private final ItemStack costItem;
    private final int amount;

    public ShopItem(Material material, String name, Material costItem, String costItemName) {
        this.amount = 1;
        this.costItem = new ItemStack(costItem, 1);

        this.item = new ItemStack(material, 1);
        this.meta = this.item.getItemMeta();
        this.meta.displayName(net.kyori.adventure.text.Component.text(name,
                net.kyori.adventure.text.format.NamedTextColor.GOLD));
        this.meta.lore(new ArrayList<>(
                List.of(Component.text(costItemName).color(NamedTextColor.GOLD))
        ));
        this.item.setItemMeta(this.meta);
    }

    public ShopItem(Material material, String name, Material costItem, String costItemName, int amount) {
        this.amount = amount;
        this.costItem = new ItemStack(costItem, amount);

        this.item = new ItemStack(material, 1);
        this.meta = this.item.getItemMeta();
        this.meta.displayName(net.kyori.adventure.text.Component.text(name,
                net.kyori.adventure.text.format.NamedTextColor.GOLD));
        this.meta.lore(new ArrayList<>(
                List.of(Component.text(costItemName).color(NamedTextColor.GOLD))
        ));
        this.item.setItemMeta(this.meta);
    }

    public ItemStack getItem() {
        return item;
    }

    public Material getCostItem() {
        return costItem.getType();
    }

    public static final ShopItem[] duelItems = new ShopItem[] {
            new ShopItem(Material.NETHERITE_SWORD, "Sword Duel Ticket", Material.RED_GLAZED_TERRACOTTA, "Cost: 1x Sword duel resource"),
            new ShopItem(Material.NETHERITE_BOOTS, "Parkour Duel Ticket", Material.ORANGE_GLAZED_TERRACOTTA, "Cost: 1x Parkour duel resource"),
            new ShopItem(Material.ZOMBIE_HEAD, "PVE Duel Ticket", Material.LIME_GLAZED_TERRACOTTA, "Cost: 1x PVE duel resource"),
            new ShopItem(Material.STICK, "Knockback Duel Ticket", Material.GREEN_GLAZED_TERRACOTTA, "Cost: 1x Knockback duel resource"),
    };

    public static final ShopItem[] buffItems = new ShopItem[] {
            new ShopItem(Material.SHIELD, "Shield", Material.CYAN_GLAZED_TERRACOTTA, "Cost: 2x Protection resource", 2),
            new ShopItem(Material.RECOVERY_COMPASS, "Swap", Material.CYAN_GLAZED_TERRACOTTA, "Cost: 2x Protection resource", 2),
            new ShopItem(Material.SUNFLOWER, "Respawn coin, GOD's bless", Material.CYAN_GLAZED_TERRACOTTA, "Cost: 2x Protection resource", 2),
    };

    public static ShopItem getShopItem(ItemStack item) {
        for (ShopItem shopItem : duelItems) {
            if (shopItem.getItem().getType() == item.getType()) {
                return shopItem;
            }
        }

        for (ShopItem shopItem : buffItems) {
            if (shopItem.getItem().getType() == item.getType()) {
                return shopItem;
            }
        }

        return null;
    }

    public int getCostItemAmount() {
        return amount;
    }
}
