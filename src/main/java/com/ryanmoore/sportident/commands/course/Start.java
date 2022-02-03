package com.ryanmoore.sportident.commands.course;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.util.ArrayList;

import com.ryanmoore.sportident.Main;
import com.ryanmoore.sportident.classes.SIField;
import com.ryanmoore.sportident.classes.SIStation;
import com.ryanmoore.sportident.database.SportIdent;

public class Start extends SIStation implements SIField {

    public Start(Block c) {
        super(c);
    }

    @Override
    public void onSportIdentInRange(Player competitor, ItemStack sportIdentItem) {

        try {
            SportIdent sportIdent = Main.database.getReference(sportIdentItem);
            logToStation("Already started");
        } catch (Exception e) { // no reference
            Main.database.addReference(sportIdentItem);
            SportIdent sportIdent = null;
            try {
                sportIdent = Main.database.getReference(sportIdentItem);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            sportIdent.splits.startTime = LocalTime.now();
            punch(competitor, Main.config.startMessage);
        }
    }
}
