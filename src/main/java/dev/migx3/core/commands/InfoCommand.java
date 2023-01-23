package dev.migx3.core.commands;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Ban;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.services.Service;
import dev.migx3.core.api.repositories.exception.ObjectNotFoundException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InfoCommand extends Command {

    private final Service<User> userService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public InfoCommand(Core plugin) {
        super("info", "", "information");

        userService = plugin.getUserService();

        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("Only players can execute this command").color(ChatColor.RED).create());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length != 1) {
            sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Usage: " + ChatColor.RED + "/info <player>").create());
            return;
        }

        User senderUser = userService.findOne("id", player.getUniqueId().toString());

        if (!senderUser.getRank().getPermissions().contains("mx3core.info.permission")) {
            sender.sendMessage(new ComponentBuilder("You don't have sufficient permissions to execute this command").color(ChatColor.RED).create());
            return;
        }

        String playerName = args[0];
        User user;

        try {
            user = userService.findOne("name", playerName);
        } catch (ObjectNotFoundException exception) {
            sender.sendMessage(new ComponentBuilder(exception.getMessage()).color(ChatColor.RED).create());
            return;
        }

        Rank rank = user.getRank();
        ChatColor rankColor = ChatColor.of(rank.getColor());
        List<Ban> bans = user.getBans();

        sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Name: " + rankColor + user.getName()).create());
        sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Rank: " + rankColor + rank.getName()).create());
        sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "ID: " + ChatColor.GOLD + user.getId()).create());
        sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "IP: " + ChatColor.GOLD + user.getIp()).create());

        if (bans.isEmpty()) {
            sender.sendMessage(new ComponentBuilder("NO BANS").color(ChatColor.GREEN).create());
            return;
        }

        bans.forEach(ban -> {
            String reason = ban.getReason();
            Date date = ban.getDate();
            boolean valid = ban.isValid();

            sender.sendMessage(new ComponentBuilder("\n" + ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).create());
            sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Date: " + ChatColor.RED + dateFormat.format(date)).create());
            sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Valid: " + (valid ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO")).create());
        });
    }
}