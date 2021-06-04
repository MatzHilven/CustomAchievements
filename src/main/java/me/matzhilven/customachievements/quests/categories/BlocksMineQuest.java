package me.matzhilven.customachievements.quests.categories;

import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlocksMineQuest extends AbstractQuest {

    private final Material mineBlock;

    public BlocksMineQuest(String configID, Material material, String activeName, String finishedName, List<String> lore,
                           List<World> blacklistedWorlds, List<String> rewards, int amountNeeded, Material mineBlock,
                           QuestType questType) {
        super(configID, material, activeName, finishedName, lore, blacklistedWorlds, rewards, Category.MINEBLOCKS,
                questType, amountNeeded);
        this.mineBlock = mineBlock;
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        if (isActive()) {
            if (getBlacklistedWorlds().contains(e.getPlayer().getWorld())) return;
            if (!e.getBlock().getType().equals(mineBlock)) return;
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
