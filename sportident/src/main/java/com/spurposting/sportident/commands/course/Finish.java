package com.spurposting.sportident.commands.course;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.classes.SplitsBook;
import com.spurposting.sportident.classes.SplitsValidator;
import com.spurposting.sportident.classes.SIStation;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.util.ArrayList;

public class Finish extends SIStation implements CommandExecutor {

    public Finish(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        ArrayList<Player> competitors = this.getNearbyCompetitors();

        for (Player competitor : competitors) {
            ItemStack sportIdentItem = this.getSportIdent(competitor);
            SportIdent sportIdent = null;
            try {
                sportIdent = Main.database.getReference(sportIdentItem);
            } catch (Exception ignored) {}

            // if not has already punched finish
            if (sportIdent.splits.finishTime == null) {
                LocalTime now = LocalTime.now();

                sportIdent.splits.finishTime = now;

                ((Player) competitor).sendMessage(Main.config.finishMessage);
                ((Player) competitor).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);

                SplitsValidator sv = new SplitsValidator(Main.config.courseOrder.toArray(new Integer[0]));
                Boolean status = sv.validate(sportIdent.splits);
                SplitsBook results = new SplitsBook(sportIdent, competitor, status);
                competitor.getInventory().addItem(results.getBook());

                Main.database.deleteReference(sportIdentItem);
            }
        }
        return true;
    }
}