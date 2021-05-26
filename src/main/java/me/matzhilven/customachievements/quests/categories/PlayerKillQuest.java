package me.matzhilven.customachievements.quests.categories;

import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class PlayerKillQuest extends AbstractQuest {

    public PlayerKillQuest(String configID, Material material, String activeName, String finishedName, List<String> lore,
                           List<World> blacklistedWorlds, List<String> rewards, int amountNeeded, QuestType questType) {
        super(configID, material, activeName, finishedName, lore, blacklistedWorlds, rewards, Category.KILLPLAYERS,
                questType, amountNeeded);
    }


    @EventHandler
    private void onPlayerKill(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();

        if (killer == null) return;

        if (isActive()) {
            if (getBlacklistedWorlds().contains(killer.getWorld())) return;
            if (hasCompleted(killer)) return;

            addPoint(killer);
            if (getPoints(killer) == getAmountNeeded()) {
                StringUtils.sendMessage(killer, "&aYou completed the quest: &r" + getFinishedName());
                StringUtils.sendMessage(killer, "&aOpen the achievements menu to claim your reward!");

                addCompletedPlayer(killer);
            }
        }

    }
}
