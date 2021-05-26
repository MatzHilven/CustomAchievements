package me.matzhilven.customachievements.commands;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.inventories.menus.MainMenu;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.StringUtils;
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

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                StringUtils.sendMessage(sender, "&cThis command can only be executed by players!");
                return true;
            }

            MainMenu menu = new MainMenu((Player) sender);
            menu.open();
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp()) {
                StringUtils.sendMessage(sender, "&cYou don't have the permission to execute this command!");
                return true;
            }
            main.reloadConfig();
            StringUtils.sendMessage(sender, "&aSuccessfully reloaded config!");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("reset")) {
            if (!sender.isOp()) {
                StringUtils.sendMessage(sender, "&cYou don't have the permission to execute this command!");
                return true;
            }

            QuestType type = QuestType.valueOf(args[1]);

            Player player = Bukkit.getPlayer(args[2]);

            if (player == null) {
                StringUtils.sendMessage(sender, "&cCan't find specified player!");
                return true;
            }

            main.getQuestManager().getQuest(type).resetPlayer(player);

            StringUtils.sendMessage(sender, "&aRemoved the progress for &l" + player.getName() + " &aon the &l" +
                    type.toString() + " &aquest!");

        }


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
