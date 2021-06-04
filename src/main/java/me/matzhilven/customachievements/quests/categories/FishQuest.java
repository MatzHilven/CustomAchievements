package me.matzhilven.customachievements.quests.categories;

import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

public class FishQuest extends AbstractQuest {

    public FishQuest(String configID, Material material, String activeName, String finishedName, List<String> lore,
                     List<World> blacklistedWorlds, List<String> rewards, int amountNeeded, QuestType questType) {
        super(configID, material, activeName, finishedName, lore, blacklistedWorlds, rewards, Category.FISH, questType,
                amountNeeded);
    }

    @EventHandler
    private void onFish(PlayerFishEvent e) {
        if (e.getCaught() == null) return;

        if (isActive()) {
            if (getBlacklistedWorlds().contains(e.getPlayer().getWorld())) return;
            if (hasCompleted(e.getPlayer())) return;
            addPoint(e.getPlayer());
            if (getPoints(e.getPlayer()) == getAmountNeeded()) {
                StringUtils.sendMessage(e.getPlayer(), config.getString("messages.completed")
                        .replace("%quest%", getFinishedName()));

                addCompletedPlayer(e.getPlayer());
            }
        }
    }
}
