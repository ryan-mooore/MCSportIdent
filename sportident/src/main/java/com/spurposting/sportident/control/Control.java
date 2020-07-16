package com.spurposting.sportident.control;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.Split;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Control extends SIStation implements CommandExecutor {
    public Control(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Integer controlCode = Integer.parseInt(args[2]);
        int controlNumber = Integer.parseInt(args[1]);
        ArrayList<Player> competitors = this.getNearbyCompetitors("SI");

        for (Player competitor : competitors) {

            ItemStack sportIdentItem = this.getSportIdent(competitor);
            SportIdent sportIdent = null;
            try {
                sportIdent = getReference(sportIdentItem);
            } catch (Exception e) {
                e.printStackTrace();
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

                String controlPunchMessageFormatted = Main.config.controlPunchMessage.replaceAll("<code>", controlCode.toString());
                controlPunchMessageFormatted = ChatColor.translateAlternateColorCodes('&', controlPunchMessageFormatted);

                commandSender.sendMessage(competitor.toString() + " punched the control");
                competitor.sendMessage(controlPunchMessageFormatted);

                this.punch(competitor);
            }
        }
        return true;
    }
}