package dev.migx3.core.commands;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.services.Service;
import dev.migx3.core.commands.subcommands.SubCommand;
import dev.migx3.core.commands.subcommands.rank.RankCreateSubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class RankCommand extends Command {

    private final List<SubCommand> subCommands = new ArrayList<>();
    private final String rankHelpMessage = ChatColor.RED + "/rank create <color> <name> <display_name>";
    private final Service<User> userService;

    public RankCommand(Core plugin) {
        super("rank");

        userService = plugin.getUserService();
        subCommands.add(new RankCreateSubCommand(plugin));

        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("Only players can execute this command").color(ChatColor.RED).create());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        User user = userService.findOne("id", player.getUniqueId().toString());
        Rank rank = user.getRank();

        List<String> rankPermissions = rank.getPermissions();

        if (!rankPermissions.contains("mx3core.rank.permission")) {
            sender.sendMessage(new ComponentBuilder("You don't have sufficient permissions to execute this command").color(ChatColor.RED).create());
            return;
        }

        for (SubCommand subCommand : subCommands) {

            if (args.length < 1 || !subCommand.getAliases().contains(args[0].toLowerCase())) {
                sender.sendMessage(new ComponentBuilder(rankHelpMessage).create());
                return;
            }

            subCommand.execute(player, args);
        }
    }
}
