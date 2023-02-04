package dev.migx3.core.commands;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Ban;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.repositories.exception.ObjectNotFoundException;
import dev.migx3.core.api.services.Service;
import me.kbrewster.exceptions.APIException;
import me.kbrewster.exceptions.InvalidPlayerException;
import me.kbrewster.mojangapi.MojangAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BanCommand extends Command {

    private final Service<Ban> banService;
    private final Service<User> userService;

    public BanCommand(Core plugin) {
        super("ban");

        banService = plugin.getBanService();
        userService = plugin.getUserService();

        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {


        User senderUser = null;

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            senderUser = userService.findOne("id", player.getUniqueId().toString());
            Rank rank = senderUser.getRank();
            List<String> rankPermissions = rank.getPermissions();

            if (!rankPermissions.contains("mx3core.ban.command")) {
                player.sendMessage(new ComponentBuilder("You don't have permission to execute this command").color(ChatColor.RED).create());
                return;
            }
        }

        if (args.length < 2) {
            sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Usage: " + ChatColor.RED + "/ban <player> <reason>").create());
            return;
        }

        String targetName = args[0];
        UUID targetUUID;
        User targetUser;

        String[] reasonArray = Arrays.copyOfRange(args, 1, args.length);
        String reason = String.join(" ", reasonArray);

        try {
            targetUUID = MojangAPI.getUUID(targetName);
        } catch (APIException | InvalidPlayerException | NullPointerException | IOException e) {
            sender.sendMessage(new ComponentBuilder("This player doesn't exist").color(ChatColor.RED).create());
            return;
        }

        try {
            targetUser = userService.findOne("id", targetUUID.toString());
        } catch (ObjectNotFoundException exception) {
            sender.sendMessage(new ComponentBuilder(exception.getMessage()).color(ChatColor.RED).create());
            return;
        }

        Rank targetRank = targetUser.getRank();

        if (senderUser != null) {

            if (senderUser.equals(targetUser)) {
                sender.sendMessage(new ComponentBuilder("You cannot ban yourself").color(ChatColor.RED).create());
                return;
            }

            Rank senderRank = senderUser.getRank();

            if (targetRank.getLevel() > senderRank.getLevel()) {
                sender.sendMessage(new ComponentBuilder("You can't ban this player").color(ChatColor.RED).create());
                return;
            }
        }

        List<Ban> targetBans = targetUser.getBans();

        targetBans.stream().filter(Ban::isValid).findFirst().ifPresentOrElse(targetBan -> {
            sender.sendMessage(new ComponentBuilder("This player is already banned").color(ChatColor.RED).create());
        }, () -> {

            Ban ban = new Ban(targetUser.getId(), reason, true);

            targetUser.getBans().add(ban);

            userService.save(targetUser);
            banService.save(ban);

            ChatColor targetRankColor = ChatColor.of(targetRank.getColor());
            String banMessage = ChatColor.GREEN + "You have successfully banned player " + targetRankColor + targetUser.getName();
            sender.sendMessage(new ComponentBuilder(banMessage).create());
        });

    }
}
