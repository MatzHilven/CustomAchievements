package me.matzhilven.customachievements;

import lombok.Getter;
import me.matzhilven.customachievements.commands.AchievementCommand;
import me.matzhilven.customachievements.commands.PlayTimeCommand;
import me.matzhilven.customachievements.listeners.InventoryListeners;
import me.matzhilven.customachievements.listeners.PlayerListeners;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.QuestManager;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.threeten.extra.YearWeek;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public final class CustomAchievements extends JavaPlugin {

    @Getter
    private static CustomAchievements instance;

    @Getter
    private QuestManager questManager;

    private File playerDataF;
    @Getter
    private FileConfiguration playerData;

    @Override
    public void onEnable() {
        instance = this;

        questManager = new QuestManager(this);
        saveDefaultConfig();

        loadPlayerData();

        getServer().getScheduler().runTaskTimer(this, () -> {
            int configDay = this.getConfig().getInt("data.day.value");
            int configWeek = this.getConfig().getInt("data.week.value");
            int configMonth = this.getConfig().getInt("data.month.value");

            LocalDate date = LocalDate.now();

            int currentDay = date.getDayOfYear();
            int currentWeek = YearWeek.now().getWeek();
            int currentMonth = date.getMonthValue();

            if (configDay != currentDay || this.getConfig().getString("data.day.quest").equals("None")) {
                questManager.setRandomQuest(QuestType.DAILY);
            } else {
                questManager.setDailyQuest(questManager.getQuestByID(this.getConfig().getString("data.day.quest")));
            }

            if (configWeek != currentWeek || this.getConfig().getString("data.week.quest").equals("None")) {
                questManager.setRandomQuest(QuestType.WEEKLY);
            } else {
                questManager.setWeeklyQuest(questManager.getQuestByID(this.getConfig().getString("data.week.quest")));
            }

            if (configMonth != currentMonth || this.getConfig().getString("data.month.quest").equals("None")) {
                questManager.setRandomQuest(QuestType.MONTHLY);
            } else {
                questManager.setMonthlyQuest(questManager.getQuestByID(this.getConfig().getString("data.month.quest")));
            }

            this.setData();


        }, 0L, 20L * 60L * 60L);

        new AchievementCommand(this);
        new PlayTimeCommand(this);

        new InventoryListeners(this);
        new PlayerListeners(this);

        getServer().getScheduler().runTaskLater(this, () -> {
            System.out.println("Daily: " + questManager.getDailyQuest().getConfigID() + " | "
                    + questManager.getDailyQuest().getCategory().toString());
            System.out.println("Weekly: " + questManager.getWeeklyQuest().getConfigID() + " | "
                    + questManager.getWeeklyQuest().getCategory().toString());
            System.out.println("Monthly: " + questManager.getMonthlyQuest().getConfigID() + " | "
                    + questManager.getMonthlyQuest().getCategory().toString());
            loadPlayers();
        }, 20L * 2L);
    }

    private void loadPlayerData() {
        playerDataF = new File(getDataFolder(), "players.yml");

        if (!playerDataF.exists()) {
            playerDataF.getParentFile().mkdir();
            saveResource("players.yml", false);
        }

        playerData = new YamlConfiguration();

        try {
            playerData.load(playerDataF);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData() {
        try {
            playerData.save(playerDataF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            AbstractQuest dailyQuest = getQuestManager().getDailyQuest();
            AbstractQuest weeklyQuest = getQuestManager().getWeeklyQuest();
            AbstractQuest monthlyQuest = getQuestManager().getMonthlyQuest();

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
        }
    }

    private void loadPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            AbstractQuest dailyQuest = getQuestManager().getDailyQuest();
            AbstractQuest weeklyQuest = getQuestManager().getWeeklyQuest();
            AbstractQuest monthlyQuest = getQuestManager().getMonthlyQuest();

            int dailyAmount = ConfigUtils.getPlayerStats(p, QuestType.DAILY);
            int weeklyAmount = ConfigUtils.getPlayerStats(p, QuestType.WEEKLY);
            int monthlyAmount = ConfigUtils.getPlayerStats(p, QuestType.MONTHLY);

            if (dailyQuest.getAmountNeeded() == dailyAmount) {
                if (ConfigUtils.isRewarded(p, QuestType.DAILY)) {
                    dailyQuest.addRewardedPlayer(p);
                } else {
                    dailyQuest.addCompletedPlayer(p);
                }
            } else {
                dailyQuest.addActivePlayer(p,dailyAmount);
            }

            if (weeklyQuest.getAmountNeeded() == weeklyAmount) {
                if (ConfigUtils.isRewarded(p, QuestType.WEEKLY)) {
                    weeklyQuest.addRewardedPlayer(p);
                } else {
                    weeklyQuest.addCompletedPlayer(p);
                }
            } else {
                weeklyQuest.addActivePlayer(p,weeklyAmount);
            }

            if (monthlyQuest.getAmountNeeded() == monthlyAmount) {
                if (ConfigUtils.isRewarded(p, QuestType.MONTHLY)) {
                    monthlyQuest.addRewardedPlayer(p);
                } else {
                    monthlyQuest.addCompletedPlayer(p);
                }
            } else {
                monthlyQuest.addActivePlayer(p,monthlyAmount);
            }
        }
    }

    private void setData() {
        LocalDate date = LocalDate.now();

        getConfig().set("data.day.value", date.getDayOfYear());
        getConfig().set("data.week.value", YearWeek.now().getWeek());
        getConfig().set("data.month.value", date.getMonthValue());

        saveConfig();
    }
}
