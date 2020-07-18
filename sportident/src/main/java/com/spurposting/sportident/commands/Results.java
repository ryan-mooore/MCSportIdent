package com.spurposting.sportident.commands;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.Result;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.util.*;

public class Results implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) { try {
            ArrayList<Result> results = new ArrayList<>();
            for (Map.Entry<Integer, SportIdent> entry : Main.database.currentRunners.entrySet()) {
                results.add(new Result(entry.getValue().player, entry.getValue().splits.getTotalTime(), Result.Status.OK, false));
            }
            results.addAll(Main.database.finishedRunners.values());

            Comparator<Result> compareByTime = (Result o1, Result o2) -> o1.time.compareTo(o2.time);
            results.sort(compareByTime);

            ArrayList<Result> mispunches = new ArrayList<Result>();
            ArrayList<Result> removals = new ArrayList<Result>();
            ArrayList<String> lines = new ArrayList<>();

            int place = 0;
            for (Result result : results) {
                if (result.hasFinished) {
                    if (!result.status.equals(Result.Status.OK)) {
                        result.place = result.status.name();
                        removals.add(result);
                        mispunches.add(result);
                    } else {
                        place++;
                        result.place = String.valueOf(place) + ".";
                    }
                }
            }

            for (Result result : removals) {
                results.remove(result);
            }

            for (Result result : results) {
                if (result.place == null) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', String.format("&2[R] %s %s", result.player.getDisplayName(), result.time)));
                } else {
                    lines.add(ChatColor.translateAlternateColorCodes('&', String.format("&l%s %s %s", result.place, result.player.getDisplayName(), result.time)));
                }
            }
            for (Result result : mispunches) {
                lines.add(ChatColor.translateAlternateColorCodes('&', String.format("&l&4%s %s %s", result.place, result.player.getDisplayName(), result.time)));
            }

            int lower = 0;
            int higher = lines.size();

            if (strings.length == 2) {
                lower = Integer.parseInt(strings[0]) - 1;
                higher = Integer.parseInt(strings[1]);
            }
            boolean foundResult = false;
            for (int i = lower; i < higher; i++) {
                try {
                    commandSender.sendMessage(lines.get(i));
                    foundResult = true;
                } catch (IndexOutOfBoundsException ignored) {
                }
                ;
            }
            if (!foundResult) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oNo Results Yet"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
