package me.matzhilven.customachievements.inventories;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.utils.ItemBuilder;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {
    protected final CustomAchievements main = CustomAchievements.getInstance();
    protected final FileConfiguration config = main.getConfig();

    protected final Player p;
    protected Inventory inventory;

    public Menu(Player p) {
        this.p = p;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleClick(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), StringUtils.colorize(getMenuName()));
        this.setMenuItems();

        p.openInventory(inventory);
    }

    public void fillSides() {
        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 3)
                .setName("")
                .addGlow()
                .toItemStack();

        for (int i = 0; i <= 9; i++) {
            inventory.setItem(i, glass);
        }

        inventory.setItem(17, glass);
        inventory.setItem(18, glass);
        inventory.setItem(26, glass);
        inventory.setItem(27, glass);
        inventory.setItem(35, glass);
        inventory.setItem(36, glass);

        for (int i = 44; i < 54; i++) {
            inventory.setItem(i, glass);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}
