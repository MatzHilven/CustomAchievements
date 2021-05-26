package me.matzhilven.customachievements.quests.categories;

import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class MobKillQuest extends AbstractQuest {

    private final EntityType entityType;

    public MobKillQuest(String configID, Material material, String activeName, String finishedName, List<String> lore,
                        List<World> blacklistedWorlds, List<String> rewards, int amountNeeded, EntityType entityType,
                        QuestType questType) {
        super(configID, material, activeName, finishedName, lore, blacklistedWorlds, rewards, Category.KILLMOBS, questType,
                amountNeeded);
        this.entityType = entityType;
    }

    @EventHandler
    private void onMobKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player killer = e.getEntity().getKiller();

        if (isActive()) {
            if (getBlacklistedWorlds().contains(killer.getWorld())) return;
            if (!e.getEntity().getType().equals(entityType)) return;
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
