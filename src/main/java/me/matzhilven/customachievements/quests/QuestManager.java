package me.matzhilven.customachievements.quests;

import lombok.Getter;
import lombok.Setter;
import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.categories.*;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestManager {

    private final CustomAchievements main;

    @Getter
    private final List<AbstractQuest> quests = new ArrayList<>();

    @Getter @Setter
    private AbstractQuest dailyQuest;
    @Getter @Setter
    private AbstractQuest weeklyQuest;
    @Getter @Setter
    private AbstractQuest monthlyQuest;

    public QuestManager(CustomAchievements main) {
        this.main = main;
        loadQuests();
    }

    public AbstractQuest getQuest(QuestType type) {
        switch (type) {
            case DAILY:
                return dailyQuest;
            case WEEKLY:
                return weeklyQuest;
            case MONTHLY:
                return monthlyQuest;
        }
        return null;
    }

    public AbstractQuest getQuestByID(String configID) {
        return quests.stream().filter(quest -> quest.getConfigID().equalsIgnoreCase(configID)).findFirst().orElse(null);
    }

    public List<AbstractQuest> getQuestsByCategory(Category category) {
        return quests.stream().filter(quest -> quest.getCategory().equals(category)).collect(Collectors.toList());
    }

    private void loadQuests() {
        FileConfiguration config = main.getConfig();

        config.getConfigurationSection("quests").getKeys(false).forEach(quest -> {
            Category category = Category.valueOf(config.getString("quests." + quest + ".category"));
            QuestType type = QuestType.valueOf(config.getString("quests." + quest + ".type"));

            Material material = Material.matchMaterial(config.getString("quests." + quest + ".material"));

            String activeName = StringUtils.colorize(config.getString("quests." + quest + ".activeName"));
            String finishedName = StringUtils.colorize(config.getString("quests." + quest + ".finishedName"));

            List<String> lore = StringUtils.colorize(config.getStringList("quests." + quest + ".lore"));
            List<String> rewards = StringUtils.colorize(config.getStringList("quests." + quest + ".rewards"));
            List<World> blacklistedWorlds = config.getStringList("quest." + quest + ".blacklisted-worlds")
                    .stream().filter(world -> Bukkit.getWorld(world) != null).map(Bukkit::getWorld).collect(Collectors.toList());

            int amountNeeded = config.getInt("quests." + quest + ".amount-needed");

            AbstractQuest abstractQuest = null;

            switch (category) {
                case FISH:
                    abstractQuest = new FishQuest(quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,rewards,amountNeeded, type);
                    break;
                case KILLMOBS:
                    EntityType entityType = EntityType.valueOf(config.getString("quests." + quest + ".mob").toUpperCase());
                    abstractQuest = new MobKillQuest(quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,rewards,amountNeeded, entityType, type);
                    break;
                case PLAYTIME:
                    abstractQuest = new TimePlayedQuest(quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,rewards,amountNeeded, type);
                    break;
                case MINEBLOCKS:
                    Material mineBlock = Material.matchMaterial(config.getString("quests." + quest + ".block"));
                    abstractQuest = new BlocksMineQuest(quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,rewards,amountNeeded, mineBlock, type);
                    break;
                case KILLPLAYERS:
                    abstractQuest = new PlayerKillQuest(quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,rewards,amountNeeded, type);
                    break;
            }

            quests.add(abstractQuest);

        });
    }

    public void setRandomQuest(QuestType type) {
        Random r = new Random();
        switch (type) {
            case DAILY:
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbstractQuest newQuest = quests.stream().filter(quest -> quest.getQuestType() == QuestType.DAILY)
                                .collect(Collectors.toList())
                                .get(r.nextInt(quests.size()));
                        if (dailyQuest == null || !dailyQuest.getCategory().equals(newQuest.getCategory())) {
                            dailyQuest = newQuest;
                            main.getConfig().set("data.day.quest", newQuest.getConfigID());
                            main.saveConfig();
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0L, 0L);
                break;
            case WEEKLY:
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbstractQuest newQuest = quests.stream().filter(quest -> quest.getQuestType() == QuestType.WEEKLY)
                                .collect(Collectors.toList())
                                .get(r.nextInt(quests.size()));
                        if (weeklyQuest == null || !weeklyQuest.getCategory().equals(newQuest.getCategory())) {
                            weeklyQuest = newQuest;
                            main.getConfig().set("data.week.quest", newQuest.getConfigID());
                            main.saveConfig();
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0L, 0L);
                break;
            case MONTHLY:
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbstractQuest newQuest = quests.stream().filter(quest -> quest.getQuestType() == QuestType.MONTHLY)
                                .collect(Collectors.toList())
                                .get(r.nextInt(quests.size()));
                        if (monthlyQuest == null || !monthlyQuest.getCategory().equals(newQuest.getCategory())) {
                            monthlyQuest = newQuest;
                            main.getConfig().set("data.month.quest", newQuest.getConfigID());
                            main.saveConfig();
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0L, 0L);
                break;
        }
    }
}