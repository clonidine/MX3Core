package dev.migx3.core.listeners;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.repositories.exception.ObjectNotFoundException;
import dev.migx3.core.api.services.Service;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

    private final Service<User> userService;
    private static final Rank defaultRank = new Rank("Default", "", "GREEN", false);

    public ServerConnectListener(Core plugin) {
        userService = plugin.getUserService();

        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        User playerWhoConnected;

        try {
            userService.findOne("id", player.getUniqueId().toString());
        } catch (ObjectNotFoundException exception) {
            playerWhoConnected = new User(player.getUniqueId().toString(), player.getName(), defaultRank, player.getSocketAddress().toString());
            userService.save(playerWhoConnected);
        }
    }
}