package com.spurposting.sportident.control;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.Split;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Finish extends SIStation implements CommandExecutor {

    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    String finishMessage = config.getString("Finish message");

    public Finish(Block c) {
        super(c);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        ArrayList<Player> competitors = this.getNearbyCompetitors("SI");

        for (Player competitor : competitors) {
            JSONObject controlsJson = null;
            controlsJson = this.getJSON(this.getSportIdent(competitor));
            Integer lastControl = 0;

            assert controlsJson != null;
            lastControl = ((Number) controlsJson.get("lastControl")).intValue();
            // last control punched was different
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

                this.setJSON(this.getSportIdent(competitor), controlsJson);

                ((Player) competitor).sendMessage(finishMessage);
                ((Player) competitor).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);

                System.out.println(
                        competitor.getName() + " Finished the course: " + controlsJson.toJSONString());

                String startTimeStr = (String) ((HashMap) splits.get(0)).get("time");
                Duration startTime = Duration.between(LocalTime.MIN, LocalTime.parse(startTimeStr));
                String finishTimeStr = (String) ((HashMap) splits.get(splits.size() - 1)).get("time");
                Duration finishTime = Duration.between(LocalTime.MIN, LocalTime.parse(finishTimeStr));

                List<Split> splitsObj = new ArrayList<Split>();
                List<Split> newSplits = new ArrayList<Split>();


                //splits 1
                for (int i = 0; i < splits.size(); i++) {
                    String timeStr = (String) ((HashMap) splits.get(i)).get("time");

                    Duration time = Duration.between(
                            LocalTime.MIN,
                            LocalTime.parse(timeStr));

                    String lastTimeString = "";
                    Duration lastTimeDiff = Duration.ofMillis(0);
                    if (i > 0) {
                        String lastTimeStr = (String) ((HashMap) splits.get(i - 1)).get("time");
                        Duration lastTime = Duration.between(
                                LocalTime.MIN,
                                LocalTime.parse(lastTimeStr));
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
                /*for (int i = 0; i < splitsObj.size() && currentControl < controls; i++) {
                    Split currentSplit = splitsObj.get(i);
                    System.out.println(currentSplit.controlNumber + " " + currentControl);
                    if (currentSplit.controlNumber == currentControl) {
                        i++;
                        currentControl++;
                        newSplits.add(currentSplit);
                    } else {
                        i++;
                    }
                }*/
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
                Score score = objective.getScore(totalTimeString + " " + competitor.getName());
                score.setScore(1);
            }
        }
        return true;
    }
}