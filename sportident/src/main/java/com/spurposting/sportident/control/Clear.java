package com.spurposting.sportident.control;

import com.spurposting.sportident.Main;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Clear extends SIStation implements CommandExecutor {

    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    String startMessage = config.getString("Start message");

    public Clear(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        ArrayList<Player> competitors = this.getNearbyCompetitors("SI");

        for (Player competitor : competitors) {
            //if(container.has(key, PersistentDataType.STRING)) {
            //    container.remove(key);
            //    sportIdent.setItemMeta((ItemMeta)itemMetaHolder);

                this.setJSON(this.getSportIdent(competitor), new JSONObject());

                competitor.sendMessage("SI cleared!");
            //} else {
            //    competitor.sendMessage("SI already cleared!")
            ((Player) competitor).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
        }
        return true;
    }
}
