package com.ryanmoore.sportident.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import com.ryanmoore.sportident.Main;
import com.ryanmoore.sportident.database.Split;
import com.ryanmoore.sportident.database.SportIdent;

public class CurrentRunners implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (Main.database.currentRunners.isEmpty()) {
            commandSender.spigot()
                    .sendMessage(new ComponentBuilder("No runners on course").color(ChatColor.RED).bold(true).create());
        } else {
            for (Map.Entry<Integer, SportIdent> runner : Main.database.currentRunners.entrySet()) {
                String leg = "";
                if (runner.getValue().splits.controls.isEmpty()) {
                    leg = "START - 1";
                } else {
                    Split lastControl = runner.getValue().splits.controls
                            .get(runner.getValue().splits.controls.size() - 1);
                    int lastControlNumber = Main.config.courseOrder.indexOf(lastControl.controlNumber);
                    if (lastControlNumber == Main.config.courseOrder.size() - 1) {
                        leg = (lastControlNumber + 1) + " - FINISH";
                    } else {
                        leg = (lastControlNumber + 1) + " - " + (lastControlNumber + 2);
                    }
                }

                Player player = runner.getValue().player;
                commandSender.spigot().sendMessage(new ComponentBuilder(
                        String.format("%1$-20s", player.getDisplayName()))
                                .color(ChatColor.AQUA)

                                .append(String.format("%1$-20s", String.format("on leg %s", leg)))
                                .color(ChatColor.GRAY)
                                .italic(true)

                                .append("Teleport")
                                .color(ChatColor.GREEN)
                                .italic(false)
                                .bold(true)
                                .underlined(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_ENTITY,
                                        new ComponentBuilder("{name:\"" + player.getName()
                                                + "\", type:\"Player\", id:\"" + player.getUniqueId() + "\"}")
                                                        .create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/tp @p %s", player.getName())))
                                .create());
            }
        }
        return true;
    }
}
