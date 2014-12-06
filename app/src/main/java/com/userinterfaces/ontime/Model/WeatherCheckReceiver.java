package com.userinterfaces.ontime.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//import com.userinterfaces.ontime.AlarmService;
import com.userinterfaces.ontime.R;
import com.userinterfaces.ontime.RetrieveWeather;
import com.userinterfaces.ontime.Tracker;

/**
 * Created by AKresling on 14-12-04.
 */
public class WeatherCheckReceiver extends BroadcastReceiver {
//    boolean badWeather = true;
    @Override
    public void onReceive(Context context, Intent intent) {
//        System.out.println("Reached adjust receiver");
        RetrieveWeather weather;//
        Tracker track = new Tracker(context);
//        System.out.println("tracker 2r");
//        System.out.println("lat = " + track.getLatitude() + "long = " + track.getLongitude());
        weather = new RetrieveWeather(context, track.getLatitude(), track.getLongitude());

        String condition = weather.getCondition();

//        System.out.println("weather condition is " + condition);
        if(!condition.equals("") && !condition.equals("CLOUDY")) {
            Alarm.getExistingInstance().fifteenMinAdjust();
//            System.out.println("Time adjusted");
        }
//        System.out.println("done");

        // save weather condition
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.alarmData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(context.getString(R.string.weatherCondition), condition);
        editor.commit();
    }
}
