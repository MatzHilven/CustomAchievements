package me.matzhilven.customachievements.quests;

import lombok.Data;
import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.utils.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public abstract class AbstractQuest implements Listener {

    protected final CustomAchievements main = CustomAchievements.getInstance();
    private final String configID;
    private final Material material;
    private final String activeName;
    private final String finishedName;
    private final List<String> lore;
    private final List<World> blacklistedWorlds;
    private final List<String> rewards;
    private final Category category;
    private final QuestType questType;
    private final int amountNeeded;
    private HashMap<UUID, Integer> activePlayers;
    private List<UUID> completedPlayers;
    private List<UUID> rewardedPlayers;

    public AbstractQuest(String configID, Material material, String activeName, String finishedName, List<String> lore,
                         List<World> blacklistedWorlds, List<String> rewards, Category category, QuestType questType, int amountNeeded) {
        main.getServer().getPluginManager().registerEvents(this, main);

        this.rewards = rewards;
        this.questType = questType;

        this.configID = configID;
        this.material = material;
        this.activeName = activeName;
        this.finishedName = finishedName;
        this.lore = lore;
        this.blacklistedWorlds = blacklistedWorlds;
        this.category = category;
        this.amountNeeded = amountNeeded;

        this.activePlayers = new HashMap<>();
        this.completedPlayers = new ArrayList<>();
        this.rewardedPlayers = new ArrayList<>();
    }

    public void addPoint(Player p) {
        if (!activePlayers.containsKey(p.getUniqueId())) return;
        activePlayers.compute(p.getUniqueId(), (uuid, integer) -> integer += 1);
    }

    public void addPoint(UUID p) {
        if (!activePlayers.containsKey(p)) return;
        activePlayers.compute(p, (uuid, integer) -> integer += 1);
    }

    public int getPoints(Player p) {
        if (!activePlayers.containsKey(p.getUniqueId())) return amountNeeded;
        return activePlayers.get(p.getUniqueId());
    }

    public void addActivePlayer(Player p, int amount) {
        activePlayers.put(p.getUniqueId(), amount);
    }

    public void addCompletedPlayer(Player p) {

        activePlayers.remove(p.getUniqueId());
        completedPlayers.add(p.getUniqueId());
        ConfigUtils.setCompleted(p, getQuestType());
    }

    public void addRewardedPlayer(Player p) {
        completedPlayers.remove(p.getUniqueId());
        rewardedPlayers.add(p.getUniqueId());
        ConfigUtils.setRewarded(p, getQuestType());
    }

    public void removePlayer(Player p) {
        activePlayers.remove(p.getUniqueId());
        completedPlayers.remove(p.getUniqueId());
        rewardedPlayers.remove(p.getUniqueId());
    }

    public boolean hasCompleted(Player p) {
        return completedPlayers.contains(p.getUniqueId()) || rewardedPlayers.contains(p.getUniqueId());
    }

    public void resetPlayer(Player p) {
        ConfigUtils.clearPlayer(p, getQuestType());

        activePlayers.put(p.getUniqueId(),0);
        completedPlayers.remove(p.getUniqueId());
        rewardedPlayers.remove(p.getUniqueId());
    }

    public boolean isRewarded(Player p) {
        return rewardedPlayers.contains(p.getUniqueId());
    }

    public boolean isActive() {
        return main.getQuestManager().getQuestsByCategory(this.category).contains(this);
    }
}