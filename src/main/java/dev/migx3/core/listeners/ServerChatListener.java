package dev.migx3.core.listeners;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.services.Service;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerChatListener implements Listener {

    private final Service<User> userService;

    public ServerChatListener(Core plugin) {
        userService = plugin.getUserService();

        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {

        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        if (event.isCommand()) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String message = event.getMessage();

        User user = userService.findOne("id", player.getUniqueId().toString());
        Rank rank = user.getRank();

        ChatColor color = ChatColor.of(rank.getColor());
        String rankFormatted = color + rank.getDisplayName() + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message;
        BaseComponent[] baseComponents = new ComponentBuilder(rankFormatted).create();

        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(baseComponents));

        event.setCancelled(true);
    }
}
