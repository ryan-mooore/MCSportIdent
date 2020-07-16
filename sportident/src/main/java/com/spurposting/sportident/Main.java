package com.spurposting.sportident;

import com.spurposting.sportident.database.CurrentRunners;
import com.spurposting.sportident.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;
    public static Database database;
    public static Config config;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config();
        database = new Database();

        loadConfig();
        loadCommands();
    }

    void loadCommands() {
        this.getCommand("course").setExecutor(new Course());
        this.getCommand("runners").setExecutor(new CurrentRunners());
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return instance;
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}