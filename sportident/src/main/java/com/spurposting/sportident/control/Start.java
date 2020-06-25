package com.spurposting.sportident.control;

import com.spurposting.sportident.Main;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;



public class Start extends SIStation implements CommandExecutor {

    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    String startMessage = config.getString("Start message");

    public Start(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        ArrayList<Player> competitors = this.getNearbyCompetitors("SI");

        for (Player competitor : competitors) {

            JSONObject controlsJson = new JSONObject();
            JSONArray splits = new JSONArray();

            LocalTime now = LocalTime.now();

            HashMap<String, String> split = new HashMap<String, String>(3);
            split.put("controlCode", "S1");
            split.put("controlNumber", "0");
            split.put("time", now.toString());

            controlsJson.put("splits", splits);
            controlsJson.put("lastControl", 0);

            competitor.sendMessage(startMessage);
            this.setJSON(this.getSportIdent(competitor), controlsJson);
        }
        return true;
    }

}
