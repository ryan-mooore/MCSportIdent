package com.spurposting.sportident;

import java.time.Duration;

public class Split {

    int controlNumber;
    Duration controlTime;
    Duration elapsedTime;

    public Split(int controlNumber, Duration controlTime, Duration time) {
        this.controlNumber = controlNumber;
        this.controlTime = controlTime;
        this.elapsedTime = time;
    }
}