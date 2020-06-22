package com.spurposting.sportident;

import java.util.*;
import java.util.function.Predicate;
import java.time.Duration;
import java.time.LocalTime;

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
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Course implements CommandExecutor {

    NamespacedKey key = new NamespacedKey(Main.getInstance(), "splits");
    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    String sportIdentName = config.getString("SportIdent name");

    String startMessage = config.getString("Start message");
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

        Integer controlNumber = 0;
        Integer controlCode = 0;

        if (args[0].equals("control")) {
            controlNumber = Integer.parseInt(args[1]);
            controlCode = Integer.parseInt(args[2]);
        }

        if (sender instanceof BlockCommandSender) {
            try {
            Block control = ((BlockCommandSender) sender).getBlock();
            Location controlLocation = control.getLocation();

            // check for entities currently punching a control
            World world = Bukkit.getServer().getWorld(courseWorld);
            if (world == null) {
                sender.sendMessage("World \"" + courseWorld + "\" not found");
            }
            Collection<Entity> entities;
            Entity nearestEntity;
            try {
                entities = world.getNearbyEntities(controlLocation, (double)punchRadius, (double)punchHeight, (double)punchRadius, isPlayer);
                nearestEntity = (Entity)entities.toArray()[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage("No entities nearby");
                return true;
            }
            HumanEntity nearestPlayer = (HumanEntity) nearestEntity;
            for (Entity entity : entities) {

                // check for player
                if (entity instanceof Player) {
                    HumanEntity player = (HumanEntity) entity;
                    ItemStack sportIdent = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = sportIdent.getItemMeta();

                    // player is holding an SI
                    if (itemMeta.getDisplayName() == null) {
                        sender.sendMessage(player.getName() + " is not holding a SportIdent");
                        return true;
                    }
                    if (itemMeta.getDisplayName().equals(sportIdentName)) {
                        PersistentDataHolder itemMetaHolder = (PersistentDataHolder) itemMeta;
                        PersistentDataContainer container = itemMetaHolder.getPersistentDataContainer();

                        if (args[0].equals("control")) {
                            return new Control(control).onCommand(sender, command, label, args);
                        } else if (args[0].equals("start")) {
                            if (!container.has(key, PersistentDataType.STRING)) {
                                JSONObject controlsJson = new JSONObject();
                                JSONArray splits = new JSONArray();

                                LocalTime now = LocalTime.now();

                                HashMap<String, String> split = new HashMap<String, String>(3);
                                split.put("controlCode", "S1");
                                split.put("controlNumber", "0");
                                split.put("time", now.toString());

                                controlsJson.put("splits", splits);
                                controlsJson.put("lastControl", 0);

                                container.set(key, PersistentDataType.STRING, controlsJson.toJSONString());
                                sportIdent.setItemMeta((ItemMeta) itemMetaHolder);

                                ((Player) player).sendMessage(startMessage);
                            }
                        } else if (args[0].equals("finish")) {
                            String data = container.get(key, PersistentDataType.STRING);
                            Object obj = new JSONParser().parse(data);

                            JSONObject controlsJson = (JSONObject) obj;
                            JSONArray splits = (JSONArray) controlsJson.get("splits");

                            // if has already punched finish
                            if (!((HashMap) splits.get(splits.size() - 1)).get("controlCode").equals("F1")) {
                                LocalTime now = LocalTime.now();

                                HashMap<String, String> finishSplit = new HashMap<String, String>(3);
                                finishSplit.put("controlCode", "F1");
                                finishSplit.put("controlNumber", "");
                                finishSplit.put("time", now.toString());

                                splits.add(finishSplit);
                                controlsJson.put("splits", splits);

                                container.set(key, PersistentDataType.STRING, controlsJson.toJSONString());
                                sportIdent.setItemMeta((ItemMeta) itemMetaHolder);

                                ((Player) player).sendMessage(finishMessage);
                                ((Player)player).playSound(controlLocation, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);

                                System.out.println(
                                        player.getName() + " Finished the course: " + controlsJson.toJSONString());

                                String startTimeStr = (String) ((HashMap) splits.get(0)).get("time");
                                Duration startTime = Duration.between(LocalTime.MIN, LocalTime.parse(startTimeStr));
                                String finishTimeStr = (String) ((HashMap) splits.get(splits.size() - 1)).get("time");
                                Duration finishTime = Duration.between(LocalTime.MIN, LocalTime.parse(finishTimeStr));

                                List<Split> splitsObj = new ArrayList<Split>();
                                List<Split> newSplits = new ArrayList<Split>();


                                //splits 1
                                for (int i = 0; i < splits.size(); i++) {
                                    String timeStr = (String)((HashMap)splits.get(i)).get("time");

                                    Duration time = Duration.between (
                                        LocalTime.MIN ,
                                        LocalTime.parse (timeStr));

                                        String lastTimeString = "";
                                        Duration lastTimeDiff = Duration.ofMillis(0);
                                        if (i > 0) {
                                            String lastTimeStr = (String)((HashMap)splits.get(i - 1)).get("time");
                                            Duration lastTime = Duration.between (
                                                LocalTime.MIN ,
                                                LocalTime.parse (lastTimeStr));
                                            lastTimeDiff = Duration.ofMillis(
                                                time.toMillis() - lastTime.toMillis());

                                            lastTimeString = String.format("%02d:%02d.%01d",
                                            lastTimeDiff.toMinutesPart(),
                                            lastTimeDiff.toSecondsPart(),
                                            lastTimeDiff.toMillisPart());

                                        }

                                    Duration absoluteTimeDiff = Duration.ofMillis(
                                        time.toMillis() - startTime.toMillis());



                                    Split split = new Split(i, lastTimeDiff, absoluteTimeDiff);
                                    System.out.println("added split: " + i + " " + lastTimeDiff + " " + absoluteTimeDiff);
                                    splitsObj.add(split);

                                }

                                //new splits
                                int controls = 25;
                                int currentControl = 0;
                                for (int i = 0; i < splitsObj.size() && currentControl < controls; i++) {
                                    Split currentSplit = splitsObj.get(i);
                                    System.out.println(currentSplit.controlNumber + " " + currentControl);
                                    if (currentSplit.controlNumber == currentControl) {
                                        i++;
                                        currentControl++;
                                        newSplits.add(currentSplit);
                                    } else {
                                        i++;
                                    }
                                }
                                String status;
                                if (currentControl > controls) {
                                    status = "OK";
                                } else {
                                    status = "MP";
                                }
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
                                Score score = objective.getScore(totalTimeString + " " + player.getName());
                                score.setScore(1);

                            }

                        } else if (args[0].equals("clear")) {
                            if (true/*player == nearestPlayer*/) {
                                if(container.has(key, PersistentDataType.STRING)) {
                                    container.remove(key);
                                    sportIdent.setItemMeta((ItemMeta)itemMetaHolder);

                                    player.sendMessage("SI cleared!");
                                } else {
                                    player.sendMessage("SI already cleared!");
                                }
                                ((Player)player).playSound(controlLocation, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
                            }
                        }
                    } else {
                        sender.sendMessage(player.getName() + " is not holding a SportIdent");
                        return true;
                    }
                } else {
                    sender.sendMessage("No player entities nearby");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        } else {
            sender.sendMessage("Only command blocks can be implemented as controls");
            return true;
        }
        return true;
    }
}