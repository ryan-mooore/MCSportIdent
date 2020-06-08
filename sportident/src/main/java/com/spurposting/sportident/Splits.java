package com.spurposting.sportident;

import java.time.LocalTime;

public class Splits {
    LocalTime startTime;
    LocalTime finishTime;

    public Splits(LocalTime startTime, LocalTime finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}