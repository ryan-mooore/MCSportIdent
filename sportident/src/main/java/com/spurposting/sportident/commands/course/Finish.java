package com.spurposting.sportident.commands.course;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.classes.SIField;
import com.spurposting.sportident.classes.SplitsBook;
import com.spurposting.sportident.classes.SplitsValidator;
import com.spurposting.sportident.classes.SIStation;
import com.spurposting.sportident.database.Result;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.util.ArrayList;

public class Finish extends SIStation implements SIField {

    public Finish(Block c) {
        super(c);
    }

    @Override
    public void onSportIdentInRange(Player competitor, ItemStack sportIdentItem) {

        SportIdent sportIdent = null;
        try {
            sportIdent = Main.database.getReference(sportIdentItem);
        } catch (Exception ignored) {
            return;
        }

        // if not has already punched finish
        if (sportIdent != null) {
            if (sportIdent.splits.finishTime == null) {

                sportIdent.splits.finishTime = LocalTime.now();

                punch(competitor, Main.config.finishMessage);

                SplitsValidator sv = new SplitsValidator(Main.config.courseOrder.toArray(new Integer[0]));
                Result.Status status = sv.validate(sportIdent.splits);
                sportIdent.status = status;
                SplitsBook results = new SplitsBook(sportIdent, competitor, status);
                competitor.getInventory().addItem(results.getBook());

                Main.database.deleteReference(sportIdentItem);
            }
        }
    }
}