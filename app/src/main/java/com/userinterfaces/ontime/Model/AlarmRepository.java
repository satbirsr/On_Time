package com.userinterfaces.ontime.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AKresling on 14-11-30.
 */
public class AlarmRepository {
    private final String TABLE_NAME = "alarms";

    public AlarmRepository() {
        /*
        * Need to setup database connection here
         */
    }

    public void add(Alarm alarm) {
        /*
        * Add the alarm data to the database
         */
    }

    public void remove(Alarm alarm) {
        /*
        * Find the alarm in the database and remove it
         */
    }

    public Alarm getAlarmByID(int id) {
        /*
        * Need to retrieve all alarm data based on this id
        * then hydrate the data and return it
         */
        return this.hydrate(id, hour, minute, year, day, month, alarmName);

    }

    public Alarm getAlarmbyName(String alarmName) {
        /*
        * Need to find alarm data from database based on alarm name
        * then hydrate and return the alarm
         */
        return this.hydrate(id, hour, minute, year, day, month, alarmName);
    }

    public ArrayList<Alarm> getAllAlarms() {
        /*
        * Need to find all the alarm data and store in
         */
        return null;
    }

    private Alarm hydrate(
            int id,
            int hour,
            int minute,
            int year,
            int day,
            int month,
            String alarmName
    ) {
        return new Alarm(id, hour, minute, year, day, month, alarmName);
    }

}
