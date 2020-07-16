package com.spurposting.sportident.control;

import com.spurposting.sportident.Config;
import com.spurposting.sportident.Main;
import com.spurposting.sportident.SplitsBook;
import com.spurposting.sportident.SplitsValidator;
import com.spurposting.sportident.database.Splits;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.time.LocalTime;
import java.util.ArrayList;

public class Finish extends SIStation implements CommandExecutor {

    public Finish(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        ArrayList<Player> competitors = this.getNearbyCompetitors("SI");

        for (Player competitor : competitors) {
            ItemStack sportIdentItem = this.getSportIdent(competitor);
            SportIdent sportIdent = null;
            try {
                sportIdent = getReference(sportIdentItem);
            } catch (Exception ignored) {

            }

            // if not has already punched finish
            if (sportIdent.splits.finishTime == null) {
                LocalTime now = LocalTime.now();

                sportIdent.splits.finishTime = now;

                ((Player) competitor).sendMessage(Main.config.finishMessage);
                ((Player) competitor).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);

                System.out.println(competitor.getName() + " Finished the course:");

                SplitsValidator sv = new SplitsValidator(Main.config.courseOrder.toArray(new Integer[0]));
                Boolean status = sv.validate(sportIdent.splits);
                SplitsBook results = new SplitsBook(sportIdent, competitor, status);
                competitor.getInventory().addItem(results.getBook());

                deleteReference(sportIdentItem);

                /*
                newSplits.add(splitsObj.get(splitsObj.size() - 1));

                //ItemStack book = SplitsBook.create(player.getName(), startTime, finishTime, status, newSplits);
                //player.getInventory().addItem(book);

                Duration totalTime = Duration.ofMillis(
                        finishTime.toMillis() - startTime.toMillis());

                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard board = manager.getMainScoreboard();
                Objective objective = board.getObjective("Time");


                List<String> entries = new ArrayList<>(board.getEntries());

                String totalTimeString = String.format("%02d:%02d.%01d",
                        totalTime.toMinutesPart(),
                        totalTime.toSecondsPart(),
                        totalTime.toMillisPart());
                Score score = objective.getScore(totalTimeString + " " + competitor.getName());
                score.setScore(1);*/
            }
        }
        return true;
    }
}