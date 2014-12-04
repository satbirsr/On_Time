package com.userinterfaces.ontime;

import android.app.Application;

import com.userinterfaces.ontime.Model.Alarm;

import java.util.HashMap;

/**
 * Created by AKresling on 14-12-04.
 */
public class OnTimeApplication extends Application {

    private HashMap<Integer, Alarm> allAlarms;


    public void setAlarm(int id, Alarm alarm) {
        if(this.allAlarms == null) {
            this.allAlarms = new HashMap<Integer, Alarm>();
        }
        this.allAlarms.put(id, alarm);
    }

    public Alarm getAlarm(int id) {
        if(this.allAlarms == null) {
            this.allAlarms = new HashMap<Integer, Alarm>();
        }
        if(!this.allAlarms.containsKey(id)) {
            return null;
        }
        return this.allAlarms.get(id);
    }
}
