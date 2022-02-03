package com.ryanmoore.sportident.classes;

import com.ryanmoore.sportident.Main;
import com.ryanmoore.sportident.database.SportIdent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SIStation implements CommandExecutor, SIField {

    public Location location;
    public NamespacedKey key;

    Block control;
    World world;

    // instantiated onCommand
    CommandSender station;
    String[] args;

    public void logToStation(String message) {
        if (station != null) {
            station.sendMessage(message);
        }
    }

    public int getControlCode() {
        if (args != null) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public SIStation(Block block) {
        location = block.getLocation();
        control = block;
        world = block.getWorld();

        key = new NamespacedKey(Main.getInstance(), "splits");
    }

    @Override
    // this is overridden by all children classes (interface)
    public void onSportIdentInRange(Player competitor, ItemStack sportIdent) {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        station = commandSender;
        if (strings.length > 0) {
            args = strings;
        }

        ArrayList<Entity> entities = new ArrayList<>(world.getNearbyEntities(
                location,
                (double) Main.config.punchRadius,
                (double) Main.config.punchHeight,
                (double) Main.config.punchRadius,
                entity -> entity instanceof Player)); // predicate - entity has to be a player

        if (entities.isEmpty()) {
            logToStation("no entities nearby");
        } else {
            for (Entity entity : entities) {
                Player competitor = (Player) entity;

                boolean correctGameMode = true;
                if (Main.config.requireAdventureMode) {
                    correctGameMode = competitor.getGameMode().equals(GameMode.ADVENTURE);
                }

                if (getSportIdent(competitor) != null && correctGameMode) {
                    // individual functionality takes place here
                    onSportIdentInRange(competitor, getSportIdent(competitor));
                }
            }
        }
        return true;
    }

    boolean isIdent(ItemStack sportIdent) {
        if (sportIdent.hasItemMeta()) {
            assert sportIdent.getItemMeta() != null;
            ItemMeta itemMeta = sportIdent.getItemMeta();
            if (itemMeta.hasDisplayName()) {
                return sportIdent.getItemMeta().getDisplayName().equals(Main.config.sportIdentName);
            }
        }
        return false;
    }

    ItemStack getSportIdent(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] slots;
        if (Main.config.sportIdentNeedsToBeHeld) {
            // possible places = hands
            slots = new ItemStack[] { inventory.getItemInMainHand(), inventory.getItemInOffHand() };
        } else {
            // possible places = full inventory
            slots = inventory.getContents();
        }

        for (ItemStack potentialSportIdentItem : slots) {
            if (isIdent(potentialSportIdentItem))
                return potentialSportIdentItem;
        }

        return null;
    }

    public void punch(Player player, String message) {

        // punching noise
        player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);

        // send punching message
        if (message != null) {
            player.sendTitle("", ChatColor.translateAlternateColorCodes('&', message), 5, 40, 20);
        }

        // update player connection with SI TODO: refactor this
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
