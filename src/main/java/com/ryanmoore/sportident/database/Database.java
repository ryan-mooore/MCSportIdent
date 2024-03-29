package com.ryanmoore.sportident.database;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.util.HashMap;

import com.ryanmoore.sportident.Main;

public class Database {

    public Integer currentRunnersID = 0;
    NamespacedKey key;
    public HashMap<Integer, SportIdent> currentRunners = new HashMap<>();
    public HashMap<Integer, Result> finishedRunners = new HashMap<>();

    public Database() {
        this.key = new NamespacedKey(Main.getInstance(), "splits");
    }

    Integer addRunner(SportIdent runner) {
        currentRunnersID++;
        currentRunners.put(currentRunnersID, runner);
        return currentRunnersID;
    }

    public SportIdent getReference(ItemStack sportIdent) throws Exception {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;
        assert itemMeta != null;
        container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();

        if (container.has(this.key, PersistentDataType.INTEGER)) {
            Integer ID = container.get(this.key, PersistentDataType.INTEGER);
            return currentRunners.get(ID);
        } else {
            throw new Exception("Course not started yet");
        }
    }

    public void addReference(ItemStack sportIdent) {
        Integer ID = addRunner(new SportIdent());

        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;

        if (itemMeta != null) {
            container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();
            container.set(key, PersistentDataType.INTEGER, ID);
            sportIdent.setItemMeta(itemMeta);
        }
    }

    public void deleteReference(ItemStack sportIdent) {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;
        if (itemMeta != null) {
            container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();
            Integer ID = container.get(this.key, PersistentDataType.INTEGER);
            SportIdent runner = currentRunners.get(ID);
            try {
                finishedRunners.put(ID, new Result(runner.player, runner.splits.getTotalTime(), runner.status, true));
            } catch (Exception ignored) {
            }
            currentRunners.remove(ID);

        }
    }
}
