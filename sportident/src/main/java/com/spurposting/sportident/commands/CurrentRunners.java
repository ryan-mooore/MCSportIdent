package com.spurposting.sportident.commands;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CurrentRunners implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (Map.Entry<Integer, SportIdent> runner:Main.database.currentRunners.entrySet()) {
            commandSender.sendMessage(runner.getKey() + ", " + runner.getValue().toString());
        }
        return true;
    }
}
