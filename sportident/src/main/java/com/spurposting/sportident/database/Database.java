package com.spurposting.sportident.database;

import java.util.HashMap;

public class Database {
    Integer currentRunnersID = 0;
    public HashMap<Integer, SportIdent> currentRunners = new HashMap<>();

    public Integer addRunner(SportIdent runner) {
        System.out.println("ID: " + currentRunnersID);
        currentRunnersID++;
        currentRunners.put(currentRunnersID, runner);
        return currentRunnersID;
    }

    public void deleteRunner(Integer ID) {
        currentRunners.remove(ID);
    }
}
