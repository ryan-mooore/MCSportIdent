package com.spurposting.sportident;

import java.util.*;
import java.util.function.Predicate;

import com.spurposting.sportident.control.Clear;
import com.spurposting.sportident.control.Control;
import com.spurposting.sportident.control.Finish;
import com.spurposting.sportident.control.Start;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

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