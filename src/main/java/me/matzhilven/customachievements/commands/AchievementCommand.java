package me.matzhilven.customachievements.commands;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.quests.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AchievementCommand implements CommandExecutor, TabExecutor {

    private final CustomAchievements main;

    public AchievementCommand(CustomAchievements main) {
        this.main = main;
        main.getCommand("achievements").setExecutor(this);
        main.getCommand("achievements").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> cmds = new ArrayList<>();
        switch (args.length) {
            case 1:
                cmds.add("reload");
                cmds.add("reset");
                return StringUtil.copyPartialMatches(args[0], cmds, new ArrayList<>());
            case 2:
                if (args[0].equalsIgnoreCase("reset"))
                    return StringUtil.copyPartialMatches(args[1],
                            Arrays.stream(QuestType.values()).map(QuestType::toString).collect(Collectors.toList()),
                            new ArrayList<>());
            case 3:
                if (args[0].equalsIgnoreCase("reset"))
                    return StringUtil.copyPartialMatches(args[2],
                            Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()),
                            new ArrayList<>());
        }

        return null;
    }
}
