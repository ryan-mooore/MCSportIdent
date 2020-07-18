package com.spurposting.sportident.commands.course;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.classes.SIStation;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.util.ArrayList;



public class Start extends SIStation implements CommandExecutor {

    public Start(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        ArrayList<Player> competitors = this.getNearbyCompetitors();
        for (Player competitor : competitors) {
            if (competitor.getGameMode().equals(GameMode.ADVENTURE)) {

                //create reference in database
                ItemStack sportIdentItem = this.getSportIdent(competitor);

                try {
                    SportIdent sportIdent = Main.database.getReference(sportIdentItem);
                    commandSender.sendMessage("Already started");
                } catch (Exception e) { //no reference
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
            return true;
    }
}
