package com.spurposting.sportident.database;

import java.time.LocalTime;
import java.util.ArrayList;

public class Splits {
    public LocalTime startTime;
    public LocalTime finishTime;
    public ArrayList<Split> controls = new ArrayList<>();
}