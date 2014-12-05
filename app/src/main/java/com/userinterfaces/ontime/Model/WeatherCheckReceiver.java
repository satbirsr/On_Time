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
    boolean badWeather = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Reached adjust receiver");
        RetrieveWeather weather = null;
        Tracker track = new Tracker(context);
        System.out.println("tracker 2r");
        //try {
//            wait(5000);
        System.out.println("lat = " + track.getLatitude() + "long = " + track.getLongitude());
            weather = new RetrieveWeather(context, track.getLatitude(), track.getLongitude());
//            wait(5000);
        //} catch (InterruptedException e) {
          //  e.printStackTrace();
        //}
      //  assert weather != null;
        System.out.println("weather condition is " + weather.getCondition());
        if(!weather.getCondition().equals("") && !weather.getCondition().equals("CLOUDY")) {
            Alarm.getExistingInstance().fifteenMinAdjust();
            System.out.println("Time adjusted");
        }
        System.out.println("done");
    }
}
