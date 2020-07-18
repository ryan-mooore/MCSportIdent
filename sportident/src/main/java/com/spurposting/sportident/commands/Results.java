package com.spurposting.sportident.commands;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.Result;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class Results implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            ArrayList<Result> results = new ArrayList<Result>();
            for (Map.Entry<Integer, SportIdent> entry : Main.database.currentRunners.entrySet()) {
                results.add(new Result(entry.getValue().player, entry.getValue().splits.getTotalTime(), Result.Status.OK, false));
            }
            results.addAll(Main.database.finishedRunners.values());

            Comparator<Result> compareByTime = (Result o1, Result o2) -> o1.time.compareTo(o2.time);
            results.sort(compareByTime);

            commandSender.sendMessage("Results:");

            int place = 0;
            for (Result result : results) {
                if (result.hasFinished) {
                    if (result.status.equals(Result.Status.MP)) {
                        result.place = "MP";
                        results.remove(result);
                        results.add(results.size(), result);
                    } else {
                        place++;
                        result.place = String.valueOf(place);
                    }
                }
            }
            for (Result result : results) {
                if (result.place == null) {
                    commandSender.sendMessage(String.format("[R] %s %s", result.player.getDisplayName(), result.time));
                } else {
                    commandSender.sendMessage(String.format("%s %s %s", result.place, result.player.getDisplayName(), result.time));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
