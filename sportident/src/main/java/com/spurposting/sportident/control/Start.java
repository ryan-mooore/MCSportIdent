package com.spurposting.sportident.control;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        try {
            for (Player competitor : competitors) {

                //create reference in database
                ItemStack sportIdentItem = this.getSportIdent(competitor);

                try {
                    SportIdent sportIdent = getReference(sportIdentItem);
                    commandSender.sendMessage("Already started");
                } catch (Exception e) { //no reference
                    addReference(sportIdentItem);
                    SportIdent sportIdent = getReference(sportIdentItem);
                    sportIdent.splits.startTime = LocalTime.now();
                    competitor.sendMessage(startMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
