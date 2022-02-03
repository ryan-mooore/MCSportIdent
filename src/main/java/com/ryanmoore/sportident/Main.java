package com.ryanmoore.sportident;

import com.ryanmoore.sportident.commands.Course;
import com.ryanmoore.sportident.commands.CurrentRunners;
import com.ryanmoore.sportident.commands.Results;
import com.ryanmoore.sportident.commands.StartClock;
import com.ryanmoore.sportident.database.Database;

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
        this.getCommand("results").setExecutor(new Results());
        this.getCommand("startclock").setExecutor(new StartClock());
    }

    // void loadListeners() {
    // PluginManager pm = Bukkit.getServer().getPluginManager();
    // pm.registerEvents(new JoinEvent(), this);
    // }

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