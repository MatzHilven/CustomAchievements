package me.matzhilven.customachievements.quests;

import lombok.Data;
import me.matzhilven.customachievements.CustomAchievements;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

@Data
public abstract class AbstractQuest implements Listener {

    private List<UUID> activePlayers;
    private List<UUID> completedPlayers;

    private final String configID;
    private final Material material;
    private final String activeName;
    private final String finishedName;
    private final List<String> lore;
    private final List<World> blacklistedWorlds;

    private final QuestType questType;

    private final int amountNeeded;

    public AbstractQuest(CustomAchievements main, String configID, Material material, String activeName,
                         String finishedName, List<String> lore, List<World> blacklistedWorlds, QuestType questType, int amountNeeded) {
        this.configID = configID;
        this.material = material;
        this.activeName = activeName;
        this.finishedName = finishedName;
        this.lore = lore;
        this.blacklistedWorlds = blacklistedWorlds;
        this.questType = questType;
        this.amountNeeded = amountNeeded;
        main.getServer().getPluginManager().registerEvents(this, main);
    }
}