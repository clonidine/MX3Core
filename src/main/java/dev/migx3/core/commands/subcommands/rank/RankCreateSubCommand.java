package dev.migx3.core.commands.subcommands.rank;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.services.Service;
import dev.migx3.core.commands.subcommands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RankCreateSubCommand implements SubCommand {

    private final Service<Rank> rankService;

    public RankCreateSubCommand(Core plugin) {
        rankService = plugin.getRankService();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("create");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args) {

        if (args.length != 3) {
            player.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Usage: " + ChatColor.RED + "/rank create <rankName> <displayName>").create());
            return;
        }

        String rankName = args[1];
        String[] displayNameArray = Arrays.copyOfRange(args, 2, args.length);
        String displayName = String.join("", displayNameArray);
        String color = ChatColor.translateAlternateColorCodes('&', displayName);
    }
}
