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
                dailyQuest.addRewardedPlayer(p);
            } else {
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


        main.getQuestManager().getAllTimeQuests().forEach(quest -> {
            int stats = ConfigUtils.getPlayerStats(p, quest.getConfigID());
            if (quest.getAmountNeeded() == stats || ConfigUtils.hasCompletedQuest(p,quest.getConfigID())
            || ConfigUtils.isRewarded(p,quest.getConfigID())) {
                if (ConfigUtils.isRewarded(p, quest.getConfigID())) {
                quest.addRewardedPlayer(p);
            } else {
                quest.addCompletedPlayer(p);
            }
            } else {
                quest.addActivePlayer(p, stats);
            }
        });


        main.savePlayerData();

    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        AbstractQuest dailyQuest = main.getQuestManager().getDailyQuest();
        AbstractQuest weeklyQuest = main.getQuestManager().getWeeklyQuest();
        AbstractQuest monthlyQuest = main.getQuestManager().getMonthlyQuest();

        if (dailyQuest.getActivePlayers().containsKey(p.getUniqueId())) {
            ConfigUtils.setPlayerStats(p, QuestType.DAILY, dailyQuest.getPoints(p));
        }

        if (weeklyQuest.getActivePlayers().containsKey(p.getUniqueId())) {
            ConfigUtils.setPlayerStats(p, QuestType.WEEKLY, weeklyQuest.getPoints(p));
        }

        if (monthlyQuest.getActivePlayers().containsKey(p.getUniqueId())) {
            ConfigUtils.setPlayerStats(p, QuestType.MONTHLY, monthlyQuest.getPoints(p));
        }

        dailyQuest.removePlayer(p);
        weeklyQuest.removePlayer(p);
        monthlyQuest.removePlayer(p);

        main.getQuestManager().getAllTimeQuests().forEach(quest -> {
            if (quest.getActivePlayers().containsKey(p.getUniqueId())) {
                ConfigUtils.setPlayerStats(p, quest.getConfigID(), quest.getPoints(p));
            }
            quest.removePlayer(p);
        });

        main.savePlayerData();
    }
}
