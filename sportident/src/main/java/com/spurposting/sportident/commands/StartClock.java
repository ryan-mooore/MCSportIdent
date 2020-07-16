package com.spurposting.sportident.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StartClock implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int offset = 0;
        if (args.length == 4) {
            offset = Integer.parseInt(args[3]);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHoursMinutes(0, offset));

        if (args[0].equals("set") || args[0].equals("create")) {
            String name = args[1];
            String createCommand = "hd create " + name + " New StartClock";

            Bukkit.dispatchCommand(console, createCommand);

        }

        if (args[0].equals("update")) {

            String name = args[1];
            String updateCommand = "hd setline " + name + " " + args[2] + " " + dtf.format(now);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), updateCommand);
        }

        if (args[0].equals("remove")) {

            String name = args[1];
            String remove = "hd remove " + name;
            sender.sendMessage(remove);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), remove);
        }


        return true;
    }
}