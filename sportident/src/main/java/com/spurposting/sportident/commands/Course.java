package com.spurposting.sportident.commands;

import com.spurposting.sportident.commands.course.Clear;
import com.spurposting.sportident.commands.course.Control;
import com.spurposting.sportident.commands.course.Finish;
import com.spurposting.sportident.commands.course.Start;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Course implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof BlockCommandSender) {
            Block control = ((BlockCommandSender) sender).getBlock();
            switch (args[0]) {
                case "control":
                    return new Control(control).onCommand(sender, command, label, args);
                case "start":
                    return new Start(control).onCommand(sender, command, label, args);
                case "finish":
                    return new Finish(control).onCommand(sender, command, label, args);
                case "clear":
                    return new Clear(control).onCommand(sender, command, label, args);
                default:
                    sender.sendMessage("Unknown SI station type: " + args[0]);
                    return true;
            }
        } else {
            sender.sendMessage("Only command blocks can be implemented as course objects");
            return true;
        }
    }
}