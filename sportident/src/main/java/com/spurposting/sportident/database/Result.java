package com.spurposting.sportident.database;

import org.bukkit.entity.Player;

public class Result {
    public String place;
    public String time;
    public Status status;
    public Player player;
    public boolean hasFinished;

    public enum Status {
        OK,
        MP,
        DNS,
        DNF
    }

    public Result(Player player, String time, Status status, boolean hasFinished) {
        this.status = status;
        this.time = time;
        this.player = player;
        this.hasFinished = hasFinished;
    }
}
