package com.spurposting.sportident.database;

import com.spurposting.sportident.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class Database {

    Integer currentRunnersID = 0;
    NamespacedKey key;
    public HashMap<Integer, SportIdent> currentRunners = new HashMap<>();

    public Database() {
        this.key = new NamespacedKey(Main.getInstance(), "splits");
    }

    Integer addRunner(SportIdent runner) {
        System.out.println("ID: " + currentRunnersID);
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
        assert itemMeta != null;
        container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();

        container.set(key, PersistentDataType.INTEGER, ID);
        sportIdent.setItemMeta(itemMeta);
    }

    public void deleteReference(ItemStack sportIdent) {
        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;
        assert itemMeta != null;
        container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();

        Integer ID = container.get(this.key, PersistentDataType.INTEGER);
        currentRunners.remove(ID);
    }
}
