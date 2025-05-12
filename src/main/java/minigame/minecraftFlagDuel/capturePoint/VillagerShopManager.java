package minigame.minecraftFlagDuel.capturePoint;

import minigame.minecraftFlagDuel.capturePoint.entities.ShopItem;
import minigame.minecraftFlagDuel.constants.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class VillagerShopManager implements Listener {
    private final Villager[] duelVillagers = new Villager[9];
    private final Villager[] buffVillagers = new Villager[9];
    private Inventory shopInventory;

    public VillagerShopManager(Plugin plugin) {
        for (int i = 0; i < 6; ++i) {
            duelVillagers[i] = (Villager) Objects.requireNonNull(Bukkit.getWorld(Constants.worldName)).spawnEntity(
                    Constants.duelShopVillagerLocation[i], EntityType.VILLAGER
            );
            duelVillagers[i].setProfession(Villager.Profession.ARMORER);
            duelVillagers[i].setCanPickupItems(false);
            duelVillagers[i].setAI(false);
            duelVillagers[i].setInvulnerable(true);
        }
        for (int i = 0; i < 6; ++i) {
            buffVillagers[i] = (Villager) Objects.requireNonNull(Bukkit.getWorld(Constants.worldName)).spawnEntity(
                    Constants.buffShopVillagerLocation[i], EntityType.VILLAGER
            );
            buffVillagers[i].setProfession(Villager.Profession.LIBRARIAN);
            buffVillagers[i].setCanPickupItems(false);
            buffVillagers[i].setAI(false);
            buffVillagers[i].setInvulnerable(true);
        }

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        for (Villager villager : duelVillagers) {
            if (event.getRightClicked() == villager) {
                openShopInventory(event.getPlayer(), 1);
                event.setCancelled(true);
            }
        }
        for (Villager villager : buffVillagers) {
            if (event.getRightClicked() == villager) {
                openShopInventory(event.getPlayer(), 2);
                event.setCancelled(true);
            }
        }
    }

    private void openShopInventory(Player player, int type) {
        if (type == 1) { // duel Villager
            shopInventory = Bukkit.createInventory(null, 9, Component.text("商店"));

            for (ShopItem duelItem : ShopItem.duelItems) {
                shopInventory.addItem(duelItem.getItem());
            }
        }
        else if (type == 2) {
            shopInventory = Bukkit.createInventory(null, 9, Component.text("商店"));

            for (ShopItem buffItem : ShopItem.buffItems) {
                shopInventory.addItem(buffItem.getItem());
            }
        }

        player.openInventory(shopInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory() != shopInventory) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        ShopItem shopItem = ShopItem.getShopItem(item);

        if (shopItem != null && containsAtLeastByMaterial(player, shopItem.getCostItem(), shopItem.getCostItemAmount())) {
            removeItemByMaterial(player, shopItem.getCostItem(), shopItem.getCostItemAmount());
            player.getInventory().addItem(shopItem.getItem());
            player.sendMessage(Component.text("购买成功！"));
        }
        else {
            player.sendMessage(Component.text("资源不足！"));
        }
    }

    public static boolean containsAtLeastByMaterial(Player player, Material material, int amount) {
        PlayerInventory inventory = player.getInventory();
        int count = 0;

        // 遍历所有物品堆
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
                if (count >= amount) {
                    return true; // 找到了足够的数量
                }
            }
        }

        return false; // 库存中的该类型物品不足
    }

    public static void removeItemByMaterial(Player player, Material material, int amountToRemove) {
        Inventory inventory = player.getInventory();
        int removedAmount = 0;

        // 遍历所有物品堆
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack currentItem = inventory.getItem(i);
            if (currentItem != null && currentItem.getType() == material) {
                int currentAmount = currentItem.getAmount();
                if (removedAmount + currentAmount <= amountToRemove) {
                    // 如果需要移除的数量大于当前物品堆的数量，则全部移除当前物品堆
                    inventory.clear(i);
                    removedAmount += currentAmount;
                } else {
                    // 否则只移除部分
                    currentItem.setAmount(currentAmount - (amountToRemove - removedAmount));
                    inventory.setItem(i, currentItem);
                    removedAmount = amountToRemove;
                }
                if (removedAmount >= amountToRemove) break; // 已经移除了足够的数量
            }
        }

        // 刷新玩家的库存界面
        player.updateInventory();
    }
}
