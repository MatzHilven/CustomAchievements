package me.matzhilven.customachievements.listeners;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.inventories.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListeners implements Listener {

    public InventoryListeners(CustomAchievements main) {
        main.getServer().getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
            e.setCancelled(true);

            Menu menu = (Menu) holder;
            menu.handleClick(e);
        }
    }
}
