package me.matzhilven.customachievements.quests.types;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.QuestType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlocksMineQuest extends AbstractQuest {


    public BlocksMineQuest(CustomAchievements main, String configID, Material material, String activeName, String finishedName, List<String> lore, List<World> blacklistedWorlds, int amountNeeded) {
        super(main, configID, material, activeName, finishedName, lore, blacklistedWorlds, QuestType.MINEBLOCKS, amountNeeded);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {

    }
}
