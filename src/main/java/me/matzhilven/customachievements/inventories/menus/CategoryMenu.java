package me.matzhilven.customachievements.inventories.menus;

import me.matzhilven.customachievements.inventories.Menu;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.utils.ItemBuilder;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CategoryMenu extends Menu {

    private final Category category;

    public CategoryMenu(Player p, Category category) {
        super(p);
        this.category = category;
    }

    @Override
    public String getMenuName() {
        return "&cAchievements: " + category.getName();
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
            return;
        }

        AbstractQuest quest = main.getQuestManager().getAllTimeQuestsByCategory(category).get(e.getSlot());
        if (quest == null) return;
        if (quest.hasCompleted(p)) {
            if (!quest.isRewarded(p)) {
                handleRewards(quest);
                quest.addRewardedPlayer(p);

                inventory.setItem(e.getSlot(), new ItemBuilder(e.getCurrentItem())
                        .removeLoreLines(2)
                        .addLoreLine("")
                        .addLoreLine("&c&lAlready received rewards!")
                        .toItemStack());
                return;
            }
            StringUtils.sendMessage(p, "&cYou have already received the rewards for this quest!");
            return;
        }
        StringUtils.sendMessage(p, "&cYou haven't completed this quest!");

    }

    @Override
    public void setMenuItems() {
        main.getQuestManager().getAllTimeQuestsByCategory(category).forEach(quest -> {
            int amount = quest.getPoints(p);
            int total = quest.getAmountNeeded();


            ItemBuilder ib = new ItemBuilder(quest.getMaterial())
                    .setName(quest.getActiveName())
                    .setLore(quest.getLore())
                    .replace("%progress_bar%", getProgressBar(amount, total))
                    .replace("%progress_percent%", amount * 100 / total + "%")
                    .replace("%progress_value%",
                            amount + "/" + total);

            if (quest.hasCompleted(p) && quest.isRewarded(p)) {
                ib.addLoreLine("");
                ib.addLoreLine("&c&lAlready received rewards!");
            } else if (quest.hasCompleted(p) && !quest.isRewarded(p)) {
                ib.addLoreLine("");
                ib.addLoreLine("&a&lClick to receive rewards!");
            }

            inventory.addItem(ib.toItemStack());
        });

        inventory.setItem(13, new ItemBuilder(Material.BARRIER).setName("&c&lReturn")
                .setLore("", "&7Return to the", "&7achievements menu").toItemStack());
    }
}
