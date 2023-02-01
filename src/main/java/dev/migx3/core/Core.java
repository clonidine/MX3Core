package dev.migx3.core;

import com.mongodb.client.MongoClients;
import dev.migx3.core.api.domain.Ban;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.repositories.impl.BanRepository;
import dev.migx3.core.api.repositories.impl.RankRepository;
import dev.migx3.core.api.repositories.impl.UserRepository;
import dev.migx3.core.api.services.Service;
import dev.migx3.core.api.services.impl.BanService;
import dev.migx3.core.api.services.impl.RankService;
import dev.migx3.core.api.services.impl.UserService;
import dev.migx3.core.commands.*;
import dev.migx3.core.listeners.ServerChatListener;
import dev.migx3.core.listeners.ServerConnectListener;
import dev.migx3.core.listeners.ServerSwitchListener;
import dev.migx3.core.managers.ReportManager;
import dev.migx3.core.managers.StaffChatManager;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class Core extends Plugin {

    private Datastore datastore;

    private Repository<Rank> rankRepository;
    private Repository<Ban> banRepository;
    private Repository<User> userRepository;

    private Service<Ban> banService;
    private Service<User> userService;
    private Service<Rank> rankService;

    private ReportManager reportManager;
    private StaffChatManager staffChatManager;

    @Override
    public void onLoad() {
        getLogger().info(ChatColor.GREEN + "Enabling MX3Core...");

        ClassLoader classLoader = Core.class.getClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        datastore = Morphia.createDatastore(MongoClients.create(), "MX3Core");
        datastore.getMapper().mapPackage("dev.migx3.core.api.domain");
        datastore.ensureIndexes();

        loadRepositories();
        loadServices();
    }

    @Override
    public void onEnable() {
        registerManagers();
        registerListeners();
        registerCommands();

        getLogger().info(ChatColor.GREEN + "MX3Core enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "MX3Core disabled");
    }

    private void loadRepositories() {
        banRepository = new BanRepository(this);
        userRepository = new UserRepository(this);
        rankRepository = new RankRepository(this);
    }

    private void loadServices() {
        userService = new UserService(userRepository);
        banService = new BanService(banRepository);
        rankService = new RankService(rankRepository);
    }

    private void registerCommands() {
        new LobbyCommand(this);
        new InfoCommand(this);
        new RankCommand(this);
        new ReportCommand(this);
        new StaffChatCommand(this);
    }

    private void registerListeners() {
        new ServerConnectListener(this);
        new ServerChatListener(this);
        new ServerSwitchListener(this);
    }

    private void registerManagers() {
        reportManager = new ReportManager();
        staffChatManager = new StaffChatManager();
    }
}