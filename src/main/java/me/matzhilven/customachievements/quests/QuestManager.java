package me.matzhilven.customachievements.quests;

import lombok.Getter;
import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.types.*;
import me.matzhilven.customachievements.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestManager {

    private final CustomAchievements main;

    @Getter
    private final List<AbstractQuest> quests = new ArrayList<>();

    @Getter
    private AbstractQuest dailyQuest;
    @Getter
    private AbstractQuest weeklyQuest;
    @Getter
    private AbstractQuest monthlyQuest;

    public QuestManager(CustomAchievements main) {
        this.main = main;
        loadQuests();
    }


    private void loadQuests() {
        FileConfiguration config = main.getConfig();

        config.getConfigurationSection("quests").getKeys(false).forEach(quest -> {
            QuestType type = QuestType.valueOf(config.getString("quests." + quest + ".type"));

            Material material = Material.matchMaterial(config.getString("quests." + quest + ".material"));

            String activeName = StringUtil.colorize(config.getString("quests." + quest + ".activeName"));
            String finishedName = StringUtil.colorize(config.getString("quests." + quest + ".finishedName"));

            List<String> lore = StringUtil.colorize(config.getStringList("quests." + quest + ".lore"));
            List<World> blacklistedWorlds = config.getStringList("quest." + quest + ".blacklisted-worlds")
                    .stream().filter(world -> Bukkit.getWorld(world) != null).map(Bukkit::getWorld).collect(Collectors.toList());

            int amountNeeded = config.getInt("quests." + quest + ".amount-needed");

            AbstractQuest abstractQuest = null;

            switch (type) {
                case FISH:
                    abstractQuest = new FishQuest(main, quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,amountNeeded);
                    break;
                case KILLMOBS:
                    abstractQuest = new MobKillQuest(main, quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,amountNeeded);
                    break;
                case PLAYTIME:
                    abstractQuest = new TimePlayedQuest(main, quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,amountNeeded);
                    break;
                case MINEBLOCKS:
                    abstractQuest = new BlocksMineQuest(main, quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,amountNeeded);
                    break;
                case KILLPLAYERS:
                    abstractQuest = new PlayerKillQuest(main, quest,material,activeName,finishedName,
                            lore,blacklistedWorlds,amountNeeded);
                    break;
            }

            quests.add(abstractQuest);

        });
    }

    public void setRandomQuest(String type) {
        Random r = new Random();
        switch (type.toLowerCase()) {
            case "daily":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbstractQuest newQuest = quests.get(r.nextInt(quests.size()));
                        if (!dailyQuest.getQuestType().equals(newQuest.getQuestType())) {
                            dailyQuest = newQuest;
                            main.getConfig().set("data.day.quest", newQuest.getConfigID());
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0L, 0L);
                break;
            case "weekly":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbstractQuest newQuest = quests.get(r.nextInt(quests.size()));
                        if (!weeklyQuest.getQuestType().equals(newQuest.getQuestType())) {
                            weeklyQuest = newQuest;
                            main.getConfig().set("data.day.quest", newQuest.getConfigID());
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0L, 0L);
                break;
            case "monthly":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbstractQuest newQuest = quests.get(r.nextInt(quests.size()));
                        if (!monthlyQuest.getQuestType().equals(newQuest.getQuestType())) {
                            monthlyQuest = newQuest;
                            main.getConfig().set("data.day.quest", newQuest.getConfigID());
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0L, 0L);
                break;
        }
    }
}