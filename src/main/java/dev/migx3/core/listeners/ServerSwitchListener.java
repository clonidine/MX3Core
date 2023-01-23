package dev.migx3.core.listeners;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.services.Service;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {

    private final Service<User> userService;


    public ServerSwitchListener(Core plugin) {
        userService = plugin.getUserService();

        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {

    }
}
