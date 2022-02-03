package com.ryanmoore.sportident.database;

import java.time.Duration;
import java.time.LocalTime;

public class Split {

    public int controlNumber;

    public LocalTime time;
    public Duration controlTime;
    public Duration elapsedTime;

    public Split(int controlNumber, LocalTime time, Duration controlTime, Duration elapsedTime) {
        this.controlNumber = controlNumber;

        this.time = time;
        this.controlTime = controlTime;
        this.elapsedTime = elapsedTime;
    }
}