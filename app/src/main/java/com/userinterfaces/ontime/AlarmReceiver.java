package com.userinterfaces.ontime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       Intent newService = new Intent(context, AlarmService.class);
       System.out.println("Reached receiver");
       context.startService(newService);



    }
}
