package com.ryanmoore.sportident.database;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Splits {
    public LocalTime startTime = null;
    public LocalTime finishTime = null;
    public ArrayList<Split> controls = new ArrayList<>();

    String formatDuration(Duration time) {
        // return String.format("%02d:%02d",
        // time.toMinutesPart(),
        // time.toSecondsPart()/*
        // * ,
        // * time.elapsedTime.toMillisPart()
        // */);
        return "test";
    }

    public String getTotalTime() {
        if (finishTime != null) {
            return formatDuration(Duration.between(this.startTime, this.finishTime));
        } else {
            return formatDuration(Duration.between(this.startTime, LocalTime.now()));
        }
    }
}