package com.spurposting.sportident.control;

import com.spurposting.sportident.Main;
import org.bukkit.*;
import org.bukkit.block.Block;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;

import java.util.function.Predicate;

public class SIStation {

    Location location;
    Block control;
    World world;
    NamespacedKey key;

    final double punchRadius = 2;
    final double punchHeight = 2;

    public SIStation(Block c) {
        this.key = new NamespacedKey(Main.getInstance(), "splits");
        location = c.getLocation();
        control = c;
        world = c.getWorld();
    }

    Predicate<Entity> isPlayer = new Predicate<Entity>() {
        public boolean test(Entity entity) {
            return entity instanceof Player;
        }
    };

    public ItemStack getSportIdent(Player p) {
        return ((HumanEntity) p).getInventory().getItemInMainHand();
    }

    public ArrayList<Player> getNearbyCompetitors(String sportIdentName) {
        Collection<Entity> entities;
        ArrayList<Player> competitors = new ArrayList<Player>();
        Entity nearestEntity;
        entities = world.getNearbyEntities(location, (double) punchRadius, (double) punchHeight, (double) punchRadius, isPlayer);
        if (entities.isEmpty()) {
            //((CommandSender)control).sendMessage("No entities nearby");
            return competitors;
        } else {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    HumanEntity player = (HumanEntity) entity;
                    ItemStack sportIdent = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = sportIdent.getItemMeta();

                    assert itemMeta != null;
                    if (itemMeta.hasDisplayName()) {
                        if (itemMeta.getDisplayName().equals(sportIdentName)) {
                            competitors.add((Player)player);
                        }
                    }
                }
            }
        }
        return competitors;
    }

    // check for entities currently punching a control
    public JSONObject getJSON(ItemStack sportIdent) throws ParseException {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;
        if (itemMeta != null) {
            container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();
        }

        assert container != null;
        if (container.has(this.key, PersistentDataType.STRING)) {
            String data = container.get(this.key, PersistentDataType.STRING);
            Object obj;
            try {
                obj = new JSONParser().parse(data);
            } catch (ParseException e) {
                obj = new JSONObject();
            }
            return (JSONObject) obj;
        } else {
            return null;
        }
    }

    public void punch(Player player) {
        ((Player) player).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
    }

    public void setJSON(ItemStack sportIdent, JSONObject obj) {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();

        container.set(this.key, PersistentDataType.STRING, obj.toJSONString());
        sportIdent.setItemMeta((ItemMeta) (PersistentDataHolder) itemMeta);
    }
}


