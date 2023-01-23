package dev.migx3.core.commands.subcommands;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public interface SubCommand {

    List<String> getAliases();
    void execute(ProxiedPlayer player, String[] args);
}
