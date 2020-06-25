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

    NamespacedKey key = new NamespacedKey(Main.getInstance(), "splits");
    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    String sportIdentName = config.getString("SportIdent name");


    String finishMessage = config.getString("Finish message");
    String controlPunchMessage = config.getString("Control punch message");
    String notStartedMessage = config.getString("Not started message");
    String courseWorld = config.getString("World");

    Integer punchRadius = config.getInt("Punch radius");
    Integer punchHeight = config.getInt("Punch height");

    Predicate<Entity> isPlayer = new Predicate<Entity>() {
        public boolean test(Entity entity) {
            return entity instanceof Player;
        }
    };

    private Map<String, SubCommand> commands = new HashMap<>();

    public void registerCommand(String cmd, SubCommand subCommand) {
        commands.put(cmd, subCommand);
    }


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
        }
        return true;
    }
}