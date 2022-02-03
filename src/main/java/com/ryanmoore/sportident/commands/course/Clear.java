package com.ryanmoore.sportident.commands.course;

import com.ryanmoore.sportident.Main;
import com.ryanmoore.sportident.classes.SIField;
import com.ryanmoore.sportident.classes.SIStation;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Clear extends SIStation implements SIField {

    public Clear(Block c) {
        super(c);
    }

    @Override
    public void onSportIdentInRange(Player competitor, ItemStack sportIdent) {

        ItemMeta itemMeta = sportIdent.getItemMeta();
        PersistentDataContainer container = null;
        assert itemMeta != null;
        container = itemMeta.getPersistentDataContainer();

        if (container.has(this.key, PersistentDataType.INTEGER)) {
            container.remove(key);
            sportIdent.setItemMeta(itemMeta);
            competitor.sendMessage(Main.config.clearMessage);
        } else {
            competitor.sendMessage(Main.config.standbyMessage);
        }
        punch(competitor, null);
    }
}