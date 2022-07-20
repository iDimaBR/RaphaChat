package com.github.idimabr;

import com.github.idimabr.commands.ChatCommand;
import com.github.idimabr.controller.ChannelController;
import com.github.idimabr.controller.DataController;
import com.github.idimabr.listener.ChatListener;
import com.github.idimabr.listener.CommandChatListener;
import com.github.idimabr.listener.PlayerDataListener;
import com.github.idimabr.storage.SQLDatabaseFactory;
import com.github.idimabr.storage.dao.StorageRepository;
import com.github.idimabr.utils.ConfigUtil;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class RaphaChat extends JavaPlugin {

    private ConfigUtil config;
    private SQLConnector connection;
    private StorageRepository repository;
    private DataController dataController;
    private ChannelController channelController;

    @Override
    public void onLoad(){
        config = new ConfigUtil(this, "config.yml");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadStorage();
        loadListeners();
        loadCommands();
        loadControllers();
    }

    private void loadCommands(){
        getCommand("chat").setExecutor(new ChatCommand(this));
    }

    private void loadListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new CommandChatListener(this), this);
        pluginManager.registerEvents(new PlayerDataListener(this), this);

    }

    private void loadStorage() {
        connection = SQLDatabaseFactory.createConnector(config.getConfigurationSection("Database"));
        repository = new StorageRepository(this);
        repository.createTable();
    }

    private void loadControllers(){
        dataController = new DataController(this);
        channelController = new ChannelController(this);
        channelController.loadChannels();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static RaphaChat getPlugin(){
        return getPlugin(RaphaChat.class);
    }
}
