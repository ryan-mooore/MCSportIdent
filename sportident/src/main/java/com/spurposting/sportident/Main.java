package com.spurposting.sportident;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {

        instance = this;

        getLogger().info("Loading Plugin: SportIdent");
        this.getCommand("course").setExecutor(new Course());
        loadConfig();

    }

    @Override
    public void onDisable() {
        getLogger().info("Unloading Plugin: MCO");
    }

    public static Main getInstance() {
        return instance;
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}