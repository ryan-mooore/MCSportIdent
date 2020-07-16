package com.spurposting.sportident.classes;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
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

    public ItemStack getSportIdent(Player p) {
        return ((HumanEntity) p).getInventory().getItemInMainHand();
    }

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
            ((CommandSender)control).sendMessage("No competitors nearby");
            return competitors;
        } else {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    HumanEntity player = (HumanEntity) entity;
                    ItemStack sportIdent = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = sportIdent.getItemMeta();

                    assert itemMeta != null;
                    if (itemMeta.hasDisplayName()) {
                        if (itemMeta.getDisplayName().equals(Main.config.sportIdentName)) {
                            competitors.add((Player)player);
                        }
                    }
                }
            }
        }
        return competitors;
    }

    public void punch(Player player) {
        ((Player) player).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
        ItemStack sportIdentItem = ((HumanEntity) player).getInventory().getItemInMainHand();
        try {
            SportIdent sportIdent = Main.database.getReference(sportIdentItem);
            sportIdent.player = player;
        } catch (Exception ignored) {}
    }
}


