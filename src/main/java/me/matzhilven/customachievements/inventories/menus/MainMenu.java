package me.matzhilven.customachievements.inventories.menus;

import me.matzhilven.customachievements.inventories.Menu;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.utils.ItemBuilder;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends Menu {

    public MainMenu(Player p) {
        super(p);
    }

    @Override
    public String getMenuName() {
        return "&cAchievements";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        switch (e.getSlot()) {
            case 20:
                CategoryMenu categoryMenu = new CategoryMenu(p, Category.KILLPLAYERS);
                categoryMenu.open();
                break;
            case 21:
                categoryMenu = new CategoryMenu(p, Category.KILLMOBS);
                categoryMenu.open();
                break;
            case 22:
                categoryMenu = new CategoryMenu(p, Category.FISH);
                categoryMenu.open();
                break;
            case 23:
                categoryMenu = new CategoryMenu(p, Category.MINEBLOCKS);
                categoryMenu.open();
                break;
            case 24:
                categoryMenu = new CategoryMenu(p, Category.PLAYTIME);
                categoryMenu.open();
                break;
            case 30:
                if (main.getQuestManager().getDailyQuest().hasCompleted(p)) {
                    if (!main.getQuestManager().getDailyQuest().isRewarded(p)) {
                        handleRewards(main.getQuestManager().getDailyQuest());
                        main.getQuestManager().getDailyQuest().addRewardedPlayer(p);
                        inventory.setItem(e.getSlot(), new ItemBuilder(e.getCurrentItem())
                                .removeLoreLines(2)
                                .addLoreLine("")
                                .addLoreLine(config.getString("messages.already-received-rewards-menu"))
                                .toItemStack());
                        return;
                    }
                    StringUtils.sendMessage(p, config.getString("messages.already-received-rewards"));
                    break;
                }
                StringUtils.sendMessage(p, config.getString("messages.not-completed"));
                break;
            case 31:
                if (main.getQuestManager().getWeeklyQuest().hasCompleted(p)) {
                    if (!main.getQuestManager().getWeeklyQuest().isRewarded(p)) {
                        handleRewards(main.getQuestManager().getWeeklyQuest());
                        main.getQuestManager().getWeeklyQuest().addRewardedPlayer(p);
                        inventory.setItem(e.getSlot(), new ItemBuilder(e.getCurrentItem())
                                .removeLoreLines(2)
                                .addLoreLine("")
                                .addLoreLine(config.getString("messages.already-received-rewards-menu"))
                                .toItemStack());
                        return;
                    }
                    StringUtils.sendMessage(p, config.getString("messages.already-received-rewards"));
                    break;
                }
                StringUtils.sendMessage(p, config.getString("messages.not-completed"));
                break;
            case 32:
                if (main.getQuestManager().getMonthlyQuest().hasCompleted(p)) {
                    if (!main.getQuestManager().getMonthlyQuest().isRewarded(p)) {
                        handleRewards(main.getQuestManager().getMonthlyQuest());
                        main.getQuestManager().getMonthlyQuest().addRewardedPlayer(p);
                        inventory.setItem(e.getSlot(), new ItemBuilder(e.getCurrentItem())
                                .removeLoreLines(2)
                                .addLoreLine("")
                                .addLoreLine(config.getString("messages.already-received-rewards-menu"))
                                .toItemStack());
                        return;
                    }
                    StringUtils.sendMessage(p, config.getString("messages.already-received-rewards"));
                    break;
                }
                StringUtils.sendMessage(p, config.getString("messages.not-completed"));
                break;
        }
    }

    @Override
    public void setMenuItems() {
        fillSides();

        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 3)
                .setName("")
                .addGlow()
                .toItemStack();

        inventory.setItem(20, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("&c&lPlayer Kills")
                .setLore("")
                .toItemStack());
        inventory.setItem(21, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 2)
                .setName("&2&lMob Kills")
                .setLore("")
                .toItemStack());
        inventory.setItem(22, new ItemBuilder(Material.RAW_FISH)
                .setName("&b&lFish Caught")
                .setLore("")
                .toItemStack());
        inventory.setItem(23, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName("&6&lBlocks Mined")
                .setLore("")
                .toItemStack());
        inventory.setItem(24, new ItemBuilder(Material.WATCH)
                .setName("&7&lTime Played")
                .setLore("")
                .toItemStack());


        inventory.setItem(29, glass);

        AbstractQuest dailyQuest = main.getQuestManager().getDailyQuest();

        int amount = !dailyQuest.hasCompleted(p) ? dailyQuest.getPoints(p) : dailyQuest.getAmountNeeded();
        int total = dailyQuest.getAmountNeeded();

        ItemBuilder ib = new ItemBuilder(dailyQuest.getMaterial())
                .setName(dailyQuest.getActiveName())
                .setLore(dailyQuest.getLore())
                .addLoreLine("")
                .addLoreLine("&b&lDAILY QUEST")
                .replace("%progress_bar%", getProgressBar(amount, total))
                .replace("%progress_percent%", amount * 100 / total + "%")
                .replace("%progress_value%",
                        amount + "/" + total);

        if (dailyQuest.hasCompleted(p)) {
            if (dailyQuest.isRewarded(p)) {
                ib.addLoreLine("");
                ib.addLoreLine(config.getString("messages.already-received-rewards-menu"));
            } else {
                ib.addLoreLine("");
                ib.addLoreLine(config.getString("messages.receive-rewards-menu"));
            }
        }

        inventory.setItem(30, ib.toItemStack());

        AbstractQuest weeklyQuest = main.getQuestManager().getWeeklyQuest();
        amount = !weeklyQuest.hasCompleted(p) ? weeklyQuest.getPoints(p) : weeklyQuest.getAmountNeeded();
        total = weeklyQuest.getAmountNeeded();

        ib = new ItemBuilder(weeklyQuest.getMaterial())
                .setName(weeklyQuest.getActiveName())
                .setLore(weeklyQuest.getLore())
                .addLoreLine("")
                .addLoreLine("&b&lWEEKLY QUEST")
                .replace("%progress_bar%", getProgressBar(amount, total))
                .replace("%progress_percent%", amount * 100 / total + "%")
                .replace("%progress_value%",
                        amount + "/" + total);

        if (weeklyQuest.hasCompleted(p)) {
            if (weeklyQuest.isRewarded(p)) {
                ib.addLoreLine("");
                ib.addLoreLine(config.getString("messages.already-received-rewards-menu"));
            } else {
                ib.addLoreLine("");
                ib.addLoreLine(config.getString("messages.receive-rewards-menu"));
            }
        }

        inventory.setItem(31, ib.toItemStack());

        AbstractQuest monthlyQuest = main.getQuestManager().getMonthlyQuest();
        amount = !monthlyQuest.hasCompleted(p) ? monthlyQuest.getPoints(p) : monthlyQuest.getAmountNeeded();
        total = monthlyQuest.getAmountNeeded();

        ib = new ItemBuilder(monthlyQuest.getMaterial())
                .setName(monthlyQuest.getActiveName())
                .setLore(monthlyQuest.getLore())
                .addLoreLine("")
                .addLoreLine("&b&lMONTHLY QUEST")
                .replace("%progress_bar%", getProgressBar(amount, total))
                .replace("%progress_percent%", amount * 100 / total + "%")
                .replace("%progress_value%",
                        amount + "/" + total);

        if (monthlyQuest.hasCompleted(p)) {
            if (monthlyQuest.isRewarded(p)) {
                ib.addLoreLine("");
                ib.addLoreLine(config.getString("messages.already-received-rewards-menu"));
            } else {
                ib.addLoreLine("");
                ib.addLoreLine(config.getString("messages.receive-rewards-menu"));
            }
        }

        inventory.setItem(32, ib.toItemStack());
        inventory.setItem(33, glass);
    }
}
