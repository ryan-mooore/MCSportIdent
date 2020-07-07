package com.spurposting.sportident.control;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.spurposting.sportident.Main;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Control extends SIStation implements CommandExecutor {

    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    public Control(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Integer controlCode = Integer.parseInt(args[2]);
        int controlNumber = Integer.parseInt(args[1]);
        ArrayList<Player> competitors = this.getNearbyCompetitors("SI");

        for (Player competitor : competitors) {
            JSONObject controlsJson = null;
            controlsJson = this.getJSON(this.getSportIdent(competitor));
            Integer lastControl = 0;

            assert controlsJson != null;
            lastControl = ((Number) controlsJson.get("lastControl")).intValue();
            // last control punched was different
            if (!lastControl.equals(controlCode)) {
                LocalTime now = LocalTime.now();
                // add control

                JSONArray splits = (JSONArray) controlsJson.get("splits");

                HashMap<String, String> split = new HashMap<String, String>(3);
                split.put("controlCode", controlCode.toString());
                split.put("controlNumber", Integer.toString(controlNumber));
                split.put("time", now.toString());

                splits.add(split);
                controlsJson.put("splits", splits);
                controlsJson.put("lastControl", controlCode);

                String controlPunchMessage = config.getString("Control punch message");

                String controlPunchMessageFormatted = controlPunchMessage.replaceAll("<code>", controlCode.toString());
                controlPunchMessageFormatted = ChatColor.translateAlternateColorCodes('&', controlPunchMessageFormatted);
                //commandSender.sendMessage(competitor.toString() + " punched the control");
                competitor.sendMessage(controlPunchMessageFormatted);

                this.punch(competitor);
                this.setJSON(this.getSportIdent(competitor), controlsJson);
            }
        }
        return true;
    }
}