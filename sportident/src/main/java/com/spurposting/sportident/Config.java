package com.spurposting.sportident;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    public String sportIdentName = config.getString("SportIdent name");
    public String finishMessage = config.getString("Finish message");
    public String startMessage = config.getString("Start message");
    public String controlPunchMessage = config.getString("Control punch message");
    public String notStartedMessage = config.getString("Not started message");
    public String clearMessage = config.getString("Clear message");

    public String courseWorld = config.getString("World");

    public Integer punchRadius = config.getInt("Punch radius");
    public Integer punchHeight = config.getInt("Punch height");

    public String bookTitle = config.getString("Title");
    public String bookSubtitle = config.getString("Subtitle");
    public String author = config.getString("Author");

    public Boolean showChipNumber = config.getBoolean("Show chip number");
    public Boolean showAbsoluteTimes = config.getBoolean("Show absolute times");
    public Boolean showAccumulatingTimes = config.getBoolean("Show accumulating times");

    public ArrayList<Integer> courseOrder;

    public Config() {
        String courseOrderString = config.getString("Course");
        courseOrder = new ArrayList<Integer>();
        if (courseOrderString != null) {
            for (String controlCode : courseOrderString.split("[, ]")) {
                courseOrder.add(Integer.parseInt(controlCode));
            }
        }
    }
}
