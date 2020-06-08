package com.spurposting.sportident;

import java.time.LocalTime;
import java.util.HashMap;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Control {

    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    public static ItemStack run(String[] args, CommandSender sender, Location controlLocation, ItemStack sportIdent,
            NamespacedKey key, HumanEntity player) {
        try{
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataHolder itemMetaHolder = (PersistentDataHolder) itemMeta;
        PersistentDataContainer container = itemMetaHolder.getPersistentDataContainer();

        Integer controlNumber = Integer.parseInt(args[1]);
        Integer controlCode = Integer.parseInt(args[2]);

        Integer lastControl = 0;
        JSONObject controlsJson = null;

        if (container.has(key, PersistentDataType.STRING)) {
            String data = container.get(key, PersistentDataType.STRING);
            Object obj = null;
            try {
                obj = new JSONParser().parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            controlsJson = (JSONObject) obj;
        } else {
            ((Player) player).sendMessage(/*config.getString(*/"Not started message"/*)*/);
        }

        lastControl = ((Number) controlsJson.get("lastControl")).intValue();
        // last control punched was different
        if (!lastControl.equals(controlCode)) {
            LocalTime now = LocalTime.now();
            // add control

            JSONArray splits = (JSONArray) controlsJson.get("splits");

            HashMap<String, String> split = new HashMap<String, String>(3);
            split.put("controlCode", controlCode.toString());
            split.put("controlNumber", controlNumber.toString());
            split.put("time", now.toString());

            splits.add(split);
            controlsJson.put("splits", splits);
            controlsJson.put("lastControl", controlCode);

            container.set(key, PersistentDataType.STRING, controlsJson.toJSONString());
            sportIdent.setItemMeta((ItemMeta) itemMetaHolder);

            String controlPunchMessageFormatted = //config.getString("Control punch message").replaceAll("<code>",
                    //controlCode.toString());
                    "Control punch message";
            controlPunchMessageFormatted = ChatColor.translateAlternateColorCodes('&',
                    controlPunchMessageFormatted);
            sender.sendMessage(player.toString() + " punched the control");
            ((Player) player).sendMessage(controlPunchMessageFormatted);
            ((Player) player).playSound(controlLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        return sportIdent;
    }
}