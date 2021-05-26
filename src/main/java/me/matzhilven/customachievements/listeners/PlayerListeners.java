package me.matzhilven.customachievements.listeners;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.ConfigUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final CustomAchievements main;

    public PlayerListeners(CustomAchievements main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        AbstractQuest dailyQuest = main.getQuestManager().getDailyQuest();
        AbstractQuest weeklyQuest = main.getQuestManager().getWeeklyQuest();
        AbstractQuest monthlyQuest = main.getQuestManager().getMonthlyQuest();

        int dailyAmount = ConfigUtils.getPlayerStats(p, QuestType.DAILY);
        int weeklyAmount = ConfigUtils.getPlayerStats(p, QuestType.WEEKLY);
        int monthlyAmount = ConfigUtils.getPlayerStats(p, QuestType.MONTHLY);


        if (dailyQuest.getAmountNeeded() == dailyAmount || ConfigUtils.hasCompletedQuest(p,QuestType.DAILY)
            || ConfigUtils.isRewarded(p,QuestType.DAILY)) {
            if (ConfigUtils.isRewarded(p, QuestType.DAILY)) {
                System.out.println("rewarded");
                dailyQuest.addRewardedPlayer(p);
            } else {
                System.out.println("completed");
                dailyQuest.addCompletedPlayer(p);
            }
        } else {
            dailyQuest.addActivePlayer(p, dailyAmount);
        }

        if (weeklyQuest.getAmountNeeded() == weeklyAmount || ConfigUtils.hasCompletedQuest(p,QuestType.WEEKLY)
            || ConfigUtils.isRewarded(p,QuestType.WEEKLY)) {
            if (ConfigUtils.isRewarded(p, QuestType.WEEKLY)) {
                weeklyQuest.addRewardedPlayer(p);
            } else {
                weeklyQuest.addCompletedPlayer(p);
            }
        } else {
            weeklyQuest.addActivePlayer(p, weeklyAmount);
        }

        if (monthlyQuest.getAmountNeeded() == monthlyAmount || ConfigUtils.hasCompletedQuest(p,QuestType.MONTHLY)
            || ConfigUtils.isRewarded(p,QuestType.MONTHLY)){
            if (ConfigUtils.isRewarded(p, QuestType.MONTHLY)) {
                monthlyQuest.addRewardedPlayer(p);
            } else {
                monthlyQuest.addCompletedPlayer(p);
            }
        } else {
            monthlyQuest.addActivePlayer(p, monthlyAmount);
        }

        main.savePlayerData();

    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        AbstractQuest dailyQuest = main.getQuestManager().getDailyQuest();
        AbstractQuest weeklyQuest = main.getQuestManager().getWeeklyQuest();
        AbstractQuest monthlyQuest = main.getQuestManager().getMonthlyQuest();

        if (dailyQuest.getActivePlayers().containsKey(p.getUniqueId())) {
            ConfigUtils.setPlayerStats(p, QuestType.DAILY, dailyQuest.getActivePlayers().get(p.getUniqueId()));
        }

        if (weeklyQuest.getActivePlayers().containsKey(p.getUniqueId())) {
            ConfigUtils.setPlayerStats(p, QuestType.WEEKLY, weeklyQuest.getActivePlayers().get(p.getUniqueId()));
        }

        if (monthlyQuest.getActivePlayers().containsKey(p.getUniqueId())) {
            ConfigUtils.setPlayerStats(p, QuestType.MONTHLY, monthlyQuest.getActivePlayers().get(p.getUniqueId()));
        }

        dailyQuest.removePlayer(p);
        weeklyQuest.removePlayer(p);
        monthlyQuest.removePlayer(p);

        main.savePlayerData();
    }
}
