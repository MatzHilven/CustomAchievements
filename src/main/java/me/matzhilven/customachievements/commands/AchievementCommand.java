package me.matzhilven.customachievements.commands;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.inventories.menus.MainMenu;
import me.matzhilven.customachievements.quests.AbstractQuest;
import me.matzhilven.customachievements.quests.Category;
import me.matzhilven.customachievements.quests.QuestType;
import me.matzhilven.customachievements.utils.ConfigUtils;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import javax.swing.plaf.ViewportUI;
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
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp()) {
                StringUtils.sendMessage(sender, "&cYou don't have the permission to execute this command!");
                return true;
            }
            main.reloadConfig();

            main.getQuestManager().getQuests().forEach(quest -> {
                Bukkit.getOnlinePlayers().stream().filter(p -> quest.getActivePlayers().containsKey(p.getUniqueId()))
                        .forEach(p -> ConfigUtils.setPlayerStats(p, quest.getQuestType(), quest.getPoints(p)));
            });

            main.getQuestManager().getAllTimeQuests().forEach(quest -> {
                Bukkit.getOnlinePlayers().stream().filter(p -> quest.getActivePlayers().containsKey(p.getUniqueId()))
                        .forEach(p -> ConfigUtils.setPlayerStats(p, quest.getConfigID(), quest.getPoints(p)));
            });

            main.getQuestManager().loadQuests();

            main.getQuestManager().getQuests().forEach(quest -> {
                Bukkit.getOnlinePlayers().stream().filter(p -> quest.getActivePlayers().containsKey(p.getUniqueId()))
                        .forEach(p -> ConfigUtils.setPlayerStats(p, quest.getQuestType(), quest.getPoints(p)));
            });

            main.getQuestManager().getAllTimeQuests().forEach(quest -> {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    int stats = ConfigUtils.getPlayerStats(p, quest.getConfigID());
                    if (quest.getAmountNeeded() == stats || ConfigUtils.hasCompletedQuest(p,quest.getConfigID())
                            || ConfigUtils.isRewarded(p,quest.getConfigID())) {
                        if (ConfigUtils.isRewarded(p, quest.getConfigID())) {
                            quest.addRewardedPlayer(p);
                        } else {
                            quest.addCompletedPlayer(p);
                        }
                    } else {
                        quest.addActivePlayer(p, stats);
                    }
                });
            });

            AbstractQuest dailyQuest = main.getQuestManager().getDailyQuest();
            AbstractQuest weeklyQuest = main.getQuestManager().getWeeklyQuest();
            AbstractQuest monthlyQuest = main.getQuestManager().getMonthlyQuest();

            Bukkit.getOnlinePlayers().forEach(p -> {
                int dailyAmount = ConfigUtils.getPlayerStats(p, QuestType.DAILY);
                int weeklyAmount = ConfigUtils.getPlayerStats(p, QuestType.WEEKLY);
                int monthlyAmount = ConfigUtils.getPlayerStats(p, QuestType.MONTHLY);

                if (dailyQuest.getAmountNeeded() == dailyAmount || ConfigUtils.hasCompletedQuest(p,QuestType.DAILY)
                        || ConfigUtils.isRewarded(p,QuestType.DAILY)) {
                    if (ConfigUtils.isRewarded(p, QuestType.DAILY)) {
                        dailyQuest.addRewardedPlayer(p);
                    } else {
                        dailyQuest.addCompletedPlayer(p);
                    }
                } else {
                    dailyQuest.addActivePlayer(p, dailyAmount);
                }

                if (weeklyQuest.getAmountNeeded() == weeklyAmount || ConfigUtils.hasCompletedQuest(p,QuestType.WEEKLY)
                        || ConfigUtils.isRewarded(p,QuestType.WEEKLY)) {
                    if (ConfigUtils.isRewarded(p, QuestType.WEEKLY)) {
                        weeklyQuest.addRewardedPlayer(p);
                    } else {
                        weeklyQuest.addCompletedPlayer(p);
                    }
                } else {
                    weeklyQuest.addActivePlayer(p, weeklyAmount);
                }

                if (monthlyQuest.getAmountNeeded() == monthlyAmount || ConfigUtils.hasCompletedQuest(p,QuestType.MONTHLY)
                        || ConfigUtils.isRewarded(p,QuestType.MONTHLY)){
                    if (ConfigUtils.isRewarded(p, QuestType.MONTHLY)) {
                        monthlyQuest.addRewardedPlayer(p);
                    } else {
                        monthlyQuest.addCompletedPlayer(p);
                    }
                } else {
                    monthlyQuest.addActivePlayer(p, monthlyAmount);
                }
            });

            StringUtils.sendMessage(sender, "&aSuccessfully reloaded plugin!");
            return true;
        } else if (args[0].equalsIgnoreCase("reset")) {
            if (!sender.isOp()) {
                StringUtils.sendMessage(sender, "&cYou don't have the permission to execute this command!");
                return true;
            }

            if (args.length < 3) {
                StringUtils.sendMessage(sender, "&cUsage: /achievements reset <type> [id] <player>!");
                return true;
            }

            QuestType type = QuestType.valueOf(args[1]);

            if (type == QuestType.ALL_TIME) {
                Player player = Bukkit.getPlayer(args[3]);

                if (player == null) {
                    StringUtils.sendMessage(sender, "&cCan't find specified player!");
                    return true;
                }

                main.getQuestManager().getAllTimeQuestByID(args[2].toLowerCase()).resetPlayer(player);

                StringUtils.sendMessage(sender, "&aRemoved the progress for &l" + player.getName() + " &aon the &l" +
                        type.toString() + " &aquest &l" + args[2] + "!");
            } else {
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

        StringUtils.sendMessage(sender, "&cUsage: /achievements [reset/reload]");
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
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!args[1].equalsIgnoreCase("all_time")) {
                        return StringUtil.copyPartialMatches(args[2],
                                Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()),
                                new ArrayList<>());
                    } else {
                        return StringUtil.copyPartialMatches(args[2],
                                main.getQuestManager().getAllTimeQuests().stream().map(AbstractQuest::getConfigID).collect(Collectors.toList()),
                                new ArrayList<>());
                    }
                }

        }

        return null;
    }
}
