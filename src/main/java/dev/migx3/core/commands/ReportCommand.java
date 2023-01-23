package dev.migx3.core.commands;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.services.Service;
import dev.migx3.core.entities.Report;
import dev.migx3.core.managers.ReportManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportCommand extends Command {

    private final Service<User> userService;
    private final ReportManager reportManager;

    public ReportCommand(Core plugin) {
        super("report");

        userService = plugin.getUserService();
        reportManager = plugin.getReportManager();

        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("Only players can execute this command").color(ChatColor.RED).create());
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "Usage: " + ChatColor.RED + "/report <player> <reason>").create());
            return;
        }

        String playerName = args[0];
        String[] reportReasonArray = Arrays.copyOfRange(args, 1, args.length);
        String reason = String.join(" ", reportReasonArray);

        ProxiedPlayer reporter = (ProxiedPlayer) sender;
        ProxiedPlayer reported = ProxyServer.getInstance().getPlayer(playerName);

        if (reported == null) {
            sender.sendMessage(new ComponentBuilder("This player are not online.").color(ChatColor.RED).create());
            return;
        }

        if (reported.getUniqueId().equals(reporter.getUniqueId())) {
            sender.sendMessage(new ComponentBuilder("You cannot report yourself").color(ChatColor.RED).create());
            return;
        }

        Report report = reportManager.findReport(reporter.getUniqueId(), reported.getUniqueId());

        if (reportManager.getReportCooldownMap().isInCooldown(report)) {
            sender.sendMessage(
                    new ComponentBuilder(
                            "You need to wait " + reportManager.getReportCooldownMap().getRemainingTimeFormatted(report) + " to report this player again"
                    )
                            .color(ChatColor.RED).create());
            return;
        }

        User reporterUser = userService.findOne("id", reporter.getUniqueId().toString());
        Rank reporterRank = reporterUser.getRank();

        User reportedUser = userService.findOne("id", reported.getUniqueId().toString());
        Rank reportedRank = reportedUser.getRank();

        if (reportedRank.isStaff()) {
            sender.sendMessage(new ComponentBuilder("You cannot report a staff member").color(ChatColor.RED).create());
            return;
        }

        ServerInfo serverInfo = reporter.getServer().getInfo();
        String serverName = serverInfo.getName();

        ChatColor reporterRankColor = ChatColor.of(reporterRank.getColor());
        ChatColor reportedRankColor = ChatColor.of(reportedRank.getColor());

        BaseComponent[] reportBaseComponent = new ComponentBuilder("[REPORT]").color(ChatColor.AQUA).create();
        BaseComponent[] reporterNameBaseComponent = new ComponentBuilder(ChatColor.YELLOW + "Reporter: " + reporterRankColor + reporter.getName()).create();
        BaseComponent[] reportedNameBaseComponent = new ComponentBuilder(ChatColor.YELLOW + "Reported: " + reportedRankColor + reported.getName()).create();
        BaseComponent[] serverNameBaseComponent = new ComponentBuilder(ChatColor.YELLOW + "Server: " + ChatColor.AQUA + serverName).create();
        BaseComponent[] reasonBaseComponent = new ComponentBuilder(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).create();

        List<BaseComponent[]> baseComponents = new ArrayList<>(Arrays.asList(
                reportBaseComponent, reporterNameBaseComponent, reportedNameBaseComponent, serverNameBaseComponent, reasonBaseComponent
        ));

        ProxyServer.getInstance().getPlayers().stream().filter(player -> {
            User user = userService.findOne("id", player.getUniqueId().toString());
            Rank rank = user.getRank();

            return rank.isStaff();
        }).forEach(player -> baseComponents.forEach(player::sendMessage));

        reportManager.getReports().remove(report);

        report = new Report(reporter.getUniqueId(), reported.getUniqueId(), reason, LocalDate.now());

        reportManager.getReports().add(report);
        reportManager.getReportCooldownMap().put(report, 15000 * 60L);

        sender.sendMessage(new ComponentBuilder("Report submitted!").color(ChatColor.GREEN).create());
    }
}
