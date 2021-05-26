package me.matzhilven.customachievements.inventories.menus;

import me.matzhilven.customachievements.inventories.Menu;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMenu extends Menu {

    private final Category category;

    public CategoryMenu(Player p, Category category) {
        super(p);
        this.category = category;
    }

    @Override
    public String getMenuName() {
        return "&cAchievements: " + category.toString();
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if (e.getSlot() == 13) {
            MainMenu menu = new MainMenu(p);
            menu.open();
        }
    }

    @Override
    public void setMenuItems() {
        main.getQuestManager().getQuestsByCategory(category).forEach(quest -> {
            List<String> lore = quest.getLore();

            inventory.addItem(new ItemBuilder(quest.getMaterial())
                    .setName(quest.getActiveName())
                    .setLore(quest.getLore().stream().filter(line -> lore.indexOf(line) < 4)
                            .collect(Collectors.toList()))
                    .toItemStack());
        });

        inventory.setItem(13, new ItemBuilder(Material.BARRIER).setName("&c&lReturn")
                .setLore("", "&7Return to the", "&7achievements menu").toItemStack());
    }
}
