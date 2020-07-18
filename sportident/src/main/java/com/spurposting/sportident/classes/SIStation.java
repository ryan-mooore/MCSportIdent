package com.spurposting.sportident.classes;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class SIStation {

    public Location location;
    Block control;
    World world;

    public SIStation(Block block) {
        location = block.getLocation();
        control = block;
        world = block.getWorld();
    }

    public SIStation() {}

    Predicate<Entity> isPlayer = new Predicate<Entity>() {
        public boolean test(Entity entity) {
            return entity instanceof Player;
        }
    };
    public ArrayList<Player> getNearbyCompetitors() {
        Collection<Entity> entities;
        ArrayList<Player> competitors = new ArrayList<Player>();
        Entity nearestEntity;
        entities = world.getNearbyEntities(
                location,
                (double) Main.config.punchRadius,
                (double) Main.config.punchHeight,
                (double) Main.config.punchRadius,
                isPlayer);

        if (entities.isEmpty()) {
            return competitors;
        } else {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    ItemStack sportIdentItem = null;
                    if (getSportIdent(player) != null) {
                        competitors.add(player);
                    }
                }
            }
        }
        return competitors;
    }

    public ItemStack getSportIdent(Player player) {
        PlayerInventory inventory = ((HumanEntity) player).getInventory();
        if (Main.config.sportIdentNeedsToBeHeld) {
            ItemStack[] potentialSportIdentItems = {inventory.getItemInMainHand(), inventory.getItemInOffHand()};
            for (ItemStack potentialSportIdentItem : potentialSportIdentItems) {
                if (potentialSportIdentItem.hasItemMeta()) {
                    if (potentialSportIdentItem.getItemMeta().getDisplayName().equals(Main.config.sportIdentName)) {
                        return potentialSportIdentItem;
                    }
                }
            }
        } else {
            for (ItemStack potentialSportIdentItem : inventory.getContents()) {
                if (potentialSportIdentItem != null) {
                    if (potentialSportIdentItem.getItemMeta().getDisplayName().equals(Main.config.sportIdentName)) {
                        return potentialSportIdentItem;
                    }
                }
            }
        }
        return null;
    }

    public void punch(Player player, String message) {
        if (player.getGameMode().equals(GameMode.ADVENTURE)) {
            player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
            player.sendTitle("", ChatColor.translateAlternateColorCodes('&', message), 5, 40, 20);

            if (getSportIdent(player) != null) {
                SportIdent sportIdent;
                try {
                    sportIdent = Main.database.getReference(getSportIdent(player));
                    sportIdent.player = player;
                } catch (Exception ignored) {
                }
            }
        }
    }
}


