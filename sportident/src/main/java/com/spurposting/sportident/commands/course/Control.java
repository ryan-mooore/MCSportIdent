package com.spurposting.sportident.commands.course;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.classes.SIField;
import com.spurposting.sportident.classes.SIStation;
import com.spurposting.sportident.database.Split;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalTime;

public class Control extends SIStation implements SIField {
    public Control(Block c) {
        super(c);
    }

    @Override
    public void onSportIdentInRange(Player competitor, ItemStack sportIdentItem) {
        int controlCode = getControlCode();
        SportIdent sportIdent;

        try {
            sportIdent = Main.database.getReference(sportIdentItem);
            if (sportIdent.splits.controls.isEmpty()){};
        } catch (Exception e) {
            logToStation("course not started yet");
            return;
        }

        Split lastSplit;

        if (sportIdent.splits.controls.isEmpty()) { //if no control has been punched yet create pseudo-control from start-time
            lastSplit = new Split(0, sportIdent.splits.startTime, null, null);
        } else {
            lastSplit = sportIdent.splits.controls.get(sportIdent.splits.controls.size() - 1);
        }

        if (!(lastSplit.controlNumber == controlCode)) {
            LocalTime now = LocalTime.now();
            Duration controlTime = Duration.between(lastSplit.time, now);
            Duration elapsedTime = Duration.between(sportIdent.splits.startTime, now);
            sportIdent.splits.controls.add(new Split(controlCode, now, controlTime, elapsedTime));

            String controlPunchMessageFormatted = Main.config.controlPunchMessage.replaceAll("<code>", Integer.toString(controlCode));

            logToStation(competitor.toString() + " punched the control");

            this.punch(competitor, controlPunchMessageFormatted);
        }
    }
}