package com.spurposting.sportident.classes;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class SIStation {

    public Location location;
    Block control;
    World world;

    public SIStation(Block block) {
        location = block.getLocation();
        control = block;
        world = block.getWorld();
    }

    public ArrayList<Player> getNearbyCompetitors() {
        ArrayList<Player> competitors = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>(world.getNearbyEntities(
                location,
                (double) Main.config.punchRadius,
                (double) Main.config.punchHeight,
                (double) Main.config.punchRadius,
                entity -> entity instanceof Player));

        if (entities.isEmpty()) {
            return competitors;
        } else {
            for (Entity entity : entities) {
                Player competitor = (Player) entity;

                boolean correctGameMode = true;
                if (Main.config.requireAdventureMode) {
                    correctGameMode = competitor.getGameMode().equals(GameMode.ADVENTURE);
                }

                if (getSportIdent(competitor) != null && correctGameMode) {
                    competitors.add(competitor);
                }
            }
        }
        return competitors;
    }

    public boolean isIdent(ItemStack sportIdent) {
        if (sportIdent.hasItemMeta()) {
            assert sportIdent.getItemMeta() != null;
            if (sportIdent.getItemMeta().hasDisplayName()) {
                return sportIdent.getItemMeta().getDisplayName().equals(Main.config.sportIdentName);
            }
        }
        return false;
    }

    public ItemStack getSportIdent(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] slots;
        if (Main.config.sportIdentNeedsToBeHeld) {
            slots = new ItemStack[]{inventory.getItemInMainHand(), inventory.getItemInOffHand()};
        } else {
            slots = inventory.getContents();
        }

        for (ItemStack potentialSportIdentItem : slots) {
            if (isIdent(potentialSportIdentItem)) return potentialSportIdentItem;
        }

        return null;
    }

    public void punch(Player player, String message) {
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


