package me.matzhilven.customachievements.commands;

import me.matzhilven.customachievements.CustomAchievements;
import me.matzhilven.customachievements.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayTimeCommand implements CommandExecutor, TabCompleter {

    private final CustomAchievements main;
    private final FileConfiguration config;

    public PlayTimeCommand(CustomAchievements main) {
        this.main = main;
        this.config = main.getConfig();
        main.getCommand("playtime").setExecutor(this);
        main.getCommand("playtime").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            StringUtils.sendMessage(sender, config.getString("messages.no-permission"));
            return true;
        }

        if (args.length != 1) {
            StringUtils.sendMessage(sender, config.getString("messages.playtime-usage"));
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            StringUtils.sendMessage(sender, config.getString("messages.player-not-found"));
            return true;
        }

        int playedTicks = player.getStatistic(Statistic.PLAY_ONE_TICK);

        int days = playedTicks / 20 / 60 / 60 / 24;
        int hours = playedTicks / 20 / 60 / 60 % 24;
        int minutes = playedTicks / 20 / 60 % 60;


        StringUtils.sendMessage(sender, config.getString("messages.playtime")
                .replace("%player%", player.getName())
                .replace("%days%", String.valueOf(days))
                .replace("%hours%", String.valueOf(hours))
                .replace("%minutes%", String.valueOf(minutes)));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()),
                    new ArrayList<>());
        }

        return null;
    }
}
