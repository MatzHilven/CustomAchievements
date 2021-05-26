package me.matzhilven.customachievements.quests.categories;

import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TimePlayedQuest extends AbstractQuest {


    public TimePlayedQuest(String configID, Material material, String activeName, String finishedName, List<String> lore,
                           List<World> blacklistedWorlds, List<String> rewards, int amountNeeded, QuestType questType) {
        super(configID, material, activeName, finishedName, lore, blacklistedWorlds, rewards, Category.PLAYTIME,
                questType, amountNeeded);
        startTimer();
    }

    private void startTimer() {
        main.getServer().getScheduler().runTaskTimer(main, () -> {
            for (UUID player : getActivePlayers().keySet()) {
                if (Bukkit.getPlayer(player) != null) {
                    Player p = Bukkit.getPlayer(player);
                    if (getBlacklistedWorlds().contains(p.getWorld())) return;
                    addPoint(player);
                    if (getPoints(p) == getAmountNeeded()) {
                        StringUtils.sendMessage(p, "&aYou completed the quest: &r" + getFinishedName());
                        StringUtils.sendMessage(p, "&aOpen the achievements menu to claim your reward!");

                        addCompletedPlayer(p);
                    }
                }
            }
        }, 20L * 60L, 20L * 60L);
    }
}
