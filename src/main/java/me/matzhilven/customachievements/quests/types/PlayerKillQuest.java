package me.matzhilven.customachievements.quests.types;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.QuestType;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class PlayerKillQuest extends AbstractQuest {

    public PlayerKillQuest(CustomAchievements main, String configID, Material material, String activeName, String finishedName, List<String> lore, List<World> blacklistedWorlds, int amountNeeded) {
        super(main, configID, material, activeName, finishedName, lore, blacklistedWorlds, QuestType.KILLPLAYERS, amountNeeded);
    }
}
