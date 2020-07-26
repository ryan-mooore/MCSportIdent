package com.spurposting.sportident.classes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SIField {
    public void onSportIdentInRange(Player competitor, ItemStack sportIdent);
}
