package com.spurposting.sportident.classes;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.command.*;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Beeper implements CommandExecutor {

    boolean isBeeping = false;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ss");
        sender.sendMessage(dtf.format(LocalDateTime.now()));
        sender.sendMessage("Beeping status: " + isBeeping);

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if (sender instanceof BlockCommandSender) {

            BlockCommandSender senderBlock = (BlockCommandSender) sender;
            Location location = senderBlock.getBlock().getLocation();
            World world = senderBlock.getBlock().getWorld();

            /*
            if (Integer.parseInt(dtf.format(LocalDateTime.now())) >= 55 &&!isBeeping) {
                isBeeping = true;
                for (int i = 0; i < 5; i++) {
                    console.sendMessage(((Integer)i).toString());
                    /*world.playSound(location, beep, 20, 13);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isBeeping = false;
            }*/
            sender.sendMessage("here");


        } else {
            sender.sendMessage("Only command blocks can be set as beepers.");
        }
    return true;
    }
}