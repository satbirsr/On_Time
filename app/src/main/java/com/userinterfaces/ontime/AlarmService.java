package com.userinterfaces.ontime;

import android.annotation.TargetApi;
//import android.app.IntentService;
//import android.app.KeyguardManager;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
//import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
//import android.os.PowerManager;
//import android.view.WindowManager;


public class AlarmService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        System.out.println("Alarm service created");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        System.out.println("Alarm Service Start");

        Intent resultIntent = new Intent(this, Home.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Home.class);
        stackBuilder.addNextIntent(resultIntent);

//        Notification.Builder mBuilder = new Notification.Builder(this).
//                setSmallIcon(R.drawable.ic_launcher).setContentTitle("Time to wake up!").setContentText("OnTime Alarm");
//        PendingIntent pendingNotificationIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pendingNotificationIntent);
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1, mBuilder.build());

        Intent wakeUpScreen = new Intent(getApplicationContext(), WakeUpScreen.class);
        wakeUpScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(wakeUpScreen);
        stopSelf();

        return START_STICKY_COMPATIBILITY;
    }
}
