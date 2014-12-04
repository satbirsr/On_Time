package com.userinterfaces.ontime.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.userinterfaces.ontime.AlarmService;
import com.userinterfaces.ontime.RetrieveWeather;
import com.userinterfaces.ontime.Tracker;

/**
 * Created by AKresling on 14-12-04.
 */
public class WeatherCheckReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Reached adjust receiver");
        RetrieveWeather weather = null;
        Tracker track = new Tracker(context);
        try {
            wait(5000);
            weather = new RetrieveWeather(context, track.getLatitude(), track.getLongitude());
            wait(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert weather != null;
        if(!weather.getCondition().equals("")) {
            Alarm.getExistingInstance().fifteenMinAdjust();
        }
    }
}
