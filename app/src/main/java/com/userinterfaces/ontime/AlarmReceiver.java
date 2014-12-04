package com.userinterfaces.ontime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.userinterfaces.ontime.Model.Alarm;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       Intent newService = new Intent(context, AlarmService.class);
       System.out.println("Reached receiver");
        Alarm.killInstance();
       context.startService(newService);



    }
}
