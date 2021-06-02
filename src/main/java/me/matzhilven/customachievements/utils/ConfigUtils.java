package me.matzhilven.customachievements.utils;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.QuestType;
import org.bukkit.entity.Player;

public class ConfigUtils {

    private static final CustomAchievements main = CustomAchievements.getInstance();

    public static void setPlayerStats(Player player, QuestType type, int amount) {
        main.getPlayerData().set(type.toString().toLowerCase() + "-active." + player.getUniqueId(), amount);
    }

    public static int getPlayerStats(Player player, QuestType type) {
        return main.getPlayerData().getInt(type.toString().toLowerCase() + "-active." + player.getUniqueId());
    }

    public static void setCompleted(Player player, QuestType type) {
        main.getPlayerData().set(type.toString().toLowerCase() + "-active." + player.getUniqueId(), null);
        main.getPlayerData().set(type.toString().toLowerCase() + "-completed." + player.getUniqueId(), true);
    }

    public static void setRewarded(Player player, QuestType type) {
        main.getPlayerData().set(type.toString().toLowerCase() + "-completed." + player.getUniqueId(), null);
        main.getPlayerData().set(type.toString().toLowerCase() + "-rewarded." + player.getUniqueId(), true);
    }

    public static boolean hasCompletedQuest(Player player, QuestType type) {
        return main.getPlayerData().getBoolean(type.toString().toLowerCase() + "-completed." + player.getUniqueId());
    }

    public static boolean isRewarded(Player player, QuestType type) {
        return main.getPlayerData().getBoolean(type.toString().toLowerCase() + "-rewarded." + player.getUniqueId());
    }

    public static void clearPlayer(Player player, QuestType type) {
        if (type == QuestType.ALL_TIME) {
            main.getQuestManager().getAllTimeQuests().forEach(quest -> {
                main.getPlayerData().set(quest.getConfigID() + "-active." + player.getUniqueId(), 0);
                main.getPlayerData().set(quest.getConfigID() + "-completed." + player.getUniqueId(), null);
                main.getPlayerData().set(quest.getConfigID() + "-rewarded." + player.getUniqueId(), null);
            });
            return;
        }
        main.getPlayerData().set(type.toString().toLowerCase() + "-active." + player.getUniqueId(), 0);
        main.getPlayerData().set(type.toString().toLowerCase() + "-completed." + player.getUniqueId(), null);
        main.getPlayerData().set(type.toString().toLowerCase() + "-rewarded." + player.getUniqueId(), null);
        main.savePlayerData();
    }

    public static void setPlayerStats(Player player, String questID, int amount) {
        main.getPlayerData().set(questID + "-active." + player.getUniqueId(), amount);
    }

    public static int getPlayerStats(Player player, String questID) {
       return   main.getPlayerData().getInt(questID + "-active." + player.getUniqueId());
    }

    public static void setCompleted(Player player, String questID) {
        main.getPlayerData().set(questID + "-active." + player.getUniqueId(), null);
        main.getPlayerData().set(questID + "-completed." + player.getUniqueId(), true);
    }

    public static void setRewarded(Player player, String questID) {
        main.getPlayerData().set(questID + "-completed." + player.getUniqueId(), null);
        main.getPlayerData().set(questID + "-rewarded." + player.getUniqueId(), true);
    }

    public static boolean hasCompletedQuest(Player player, String questID) {
        return main.getPlayerData().getBoolean(questID + "-completed." + player.getUniqueId());
    }

    public static boolean isRewarded(Player player, String questID) {
        return main.getPlayerData().getBoolean(questID + "-rewarded." + player.getUniqueId());
    }
}
