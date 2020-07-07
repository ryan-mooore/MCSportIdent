package com.spurposting.sportident.database;

import com.spurposting.sportident.SportIdent;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    Integer currentRunnersID = 0;
    public HashMap<Integer, SportIdent> currentRunners = new HashMap<>();
    public Integer addRunner(SportIdent runner) {
        currentRunnersID++;
        currentRunners.put(currentRunnersID, runner);
        return currentRunnersID;
    }

    public void deleteRunner(Integer ID) {
        currentRunners.remove(ID);
    }
}
