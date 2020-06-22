package com.spurposting.sportident;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ControlBox {

    Location location;
    Block control;
    World world;

    final double punchRadius = 2;
    final double punchHeight = 2;

    public ControlBox (Block c) throws ParseException {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "splits");
        location = c.getLocation();
        control = c;
        world = c.getWorld();
    }

    Predicate<Entity> isPlayer = new Predicate<Entity>() {
        public boolean test(Entity entity) {
            return entity instanceof Player;
        }
    };

    public ArrayList<Player> getPunchers (String sportIdentName) throws Exception {
        Collection<Entity> entities;
        ArrayList<Player> punchers = new ArrayList<Player>();
        Entity nearestEntity;
        entities = world.getNearbyEntities(location, (double)punchRadius, (double)punchHeight, (double)punchRadius, isPlayer);
        if (entities.isEmpty()) {
            throw new Exception("No entities nearby");
        } else {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    HumanEntity player = (HumanEntity) entity;
                    ItemStack sportIdent = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = sportIdent.getItemMeta();

                    assert itemMeta != null;
                    if (itemMeta.hasDisplayName()) {
                        if (itemMeta.getDisplayName().equals(sportIdentName)) {
                            punchers.add((Player)entity);
                        } else {
                            throw new Exception(player.getName() + " is not holding a SportIdent");
                        }
                    }
                }
            }
        }
        return punchers;
    }

    // check for entities currently punching a control
    public JSONArray getJSON (ItemStack sportIdent) throws ParseException {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;
        if (itemMeta != null) {
            container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();
        }

        JSONObject controlsJson;
        Integer lastControl = 0;

        NamespacedKey key = null;

        if (container.has(key, PersistentDataType.STRING)) {
            String data = container.get(key, PersistentDataType.STRING);
            Object obj = new JSONParser().parse(data);
            controlsJson = (JSONObject) obj;
            JSONArray splits = (JSONArray) controlsJson.get("splits");
            return splits;
        } else {
            throw new NullPointerException("Bruh");
        }
    }

    public void punch(Player player) {
        String controlPunchMessage = "";
        String controlCode = "";
        String controlPunchMessageFormatted = controlPunchMessage.replaceAll("<code>", controlCode.toString());
        controlPunchMessageFormatted = ChatColor.translateAlternateColorCodes('&', controlPunchMessageFormatted);
        ((CommandSender)control).sendMessage(player.toString() + " punched the control");
        ((Player)player).sendMessage(controlPunchMessageFormatted);
        ((Player)player).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
    }

    public void setJSON(ItemStack sportIdent, JSONObject obj) {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataHolder itemMetaHolder = (PersistentDataHolder) itemMeta;
        PersistentDataContainer container = itemMetaHolder.getPersistentDataContainer();
        NamespacedKey key = null;

        container.set(key, PersistentDataType.STRING, obj.toJSONString());
        sportIdent.setItemMeta((ItemMeta)itemMetaHolder);
    }


