package dev.migx3.core.commands;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.services.Service;
import dev.migx3.core.managers.StaffChatManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class StaffChatCommand extends Command {

    private final Service<User> userService;
    private final StaffChatManager staffChatManager;

    public StaffChatCommand(Core plugin) {
        super("staffchat", "", "sc");

        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, this);

        userService = plugin.getUserService();
        staffChatManager = plugin.getStaffChatManager();
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
        ChatColor rankColor = ChatColor.of(rank.getColor());

        if (!rank.isStaff()) {
            sender.sendMessage(new ComponentBuilder("You don't have permission to execute this command").color(ChatColor.RED).create());
            return;
        }

        if (args.length == 0) {
            Set<UUID> playersInStaffChat = staffChatManager.getPlayersInStaffChat();

            if (playersInStaffChat.contains(player.getUniqueId())) {
                staffChatManager.removeFromChat(player.getUniqueId());
                player.sendMessage(new ComponentBuilder(ChatColor.RED + "You have disabled the Staff chat").create());
                return;
            }

            staffChatManager.addInChat(player.getUniqueId());
            player.sendMessage(new ComponentBuilder(ChatColor.GREEN + "You have enabled the Staff chat").create());
        } else {
            String[] messageArray = Arrays.copyOfRange(args, 0, args.length);
            String message = String.join(" ", messageArray);
            String messageFormat = rankColor + rank.getDisplayName() + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message;
            BaseComponent[] messageComponent = new ComponentBuilder(ChatColor.DARK_AQUA + "[SC] " + messageFormat).create();

            for (UUID playerUUID : staffChatManager.getPlayersInStaffChat()) {
                ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(playerUUID);
                proxiedPlayer.sendMessage(messageComponent);
            }

            player.sendMessage(new ComponentBuilder("Message sent!").color(ChatColor.GREEN).create());
        }
    }
}