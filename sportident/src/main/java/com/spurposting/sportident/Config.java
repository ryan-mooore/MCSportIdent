package com.spurposting.sportident;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    public String sportIdentName = config.getString("SportIdent name");
    public String finishMessage = config.getString("Finish message");
    public String controlPunchMessage = config.getString("Control punch message");
    public String notStartedMessage = config.getString("Not started message");
    public String courseWorld = config.getString("World");

    public Integer punchRadius = config.getInt("Punch radius");
    public Integer punchHeight = config.getInt("Punch height");
}
