package me.matzhilven.customachievements;

import lombok.Getter;
import me.matzhilven.customachievements.commands.AchievementCommand;
import me.matzhilven.customachievements.quests.QuestManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.threeten.extra.YearWeek;

import java.time.LocalDate;

public final class CustomAchievements extends JavaPlugin {

    @Getter
    private static CustomAchievements instance;

    @Getter
    private QuestManager questManager;

    @Override
    public void onEnable() {
        instance = this;

        questManager = new QuestManager(this);
        saveConfig();

        getServer().getScheduler().runTaskTimer(this, () -> {
            int configDay = this.getConfig().getInt("data.day.value");
            int configWeek = this.getConfig().getInt("data.week.value");
            int configMonth = this.getConfig().getInt("data.month.value");

            LocalDate date = LocalDate.now();

            int currentDay = date.getDayOfYear();
            int currentWeek = YearWeek.now().getWeek();
            int currentMonth = date.getMonthValue();

            if (configDay != currentDay) {
                questManager.setRandomQuest("daily");
            }

            if (configWeek != currentWeek) {
                questManager.setRandomQuest("weekly");
            }

            if (configMonth != currentMonth) {
                questManager.setRandomQuest("monthly");
            }

            this.setData();


        }, 0L, 20L * 60L * 60L);

        new AchievementCommand(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void setData() {
        LocalDate date = LocalDate.now();

        getConfig().set("data.day.value", date.getDayOfYear());
        getConfig().set("data.week.value", YearWeek.now().getWeek());
        getConfig().set("data.month.value", date.getMonthValue());

        saveConfig();
    }
}
