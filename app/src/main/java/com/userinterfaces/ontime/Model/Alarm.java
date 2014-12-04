package com.userinterfaces.ontime.Model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.userinterfaces.ontime.AddAlarm;
import com.userinterfaces.ontime.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by AKresling on 14-11-30.
 */
public class Alarm {
    private static Alarm instance;
    private int hour;
    private int minute;
    private int year;
    private int day;
    private int month;
    private String alarmName;
    private Context context;

    public Alarm(int hour, int minute, int year, int day, int month, String alarmName) {
        this.hour = hour;
        this.minute = minute;
        this.year = year;
        this.day = day;
        this.month = month;
        this.alarmName = alarmName;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public long timeInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month, this.day, this.hour, this.minute, 0);
        return cal.getTimeInMillis();
    }

    public static Alarm getExistingInstance()
    {
        return Alarm.instance;
    }

    public static Alarm getInstance(
            int hour,
            int minute,
            int year,
            int day,
            int month,
            String alarmName
    ) {
        if(Alarm.instance == null) {
            Alarm.instance = new Alarm(hour, minute, year, day, month, alarmName);
        }
        return Alarm.instance;

    }

    public void setAlarm(Context context) {
        this.context = context;
        PendingIntent pendingIntent;

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        System.out.println(this.timeInMillis());

        alarmManager.set(AlarmManager.RTC_WAKEUP, this.timeInMillis(), pendingIntent);

    }

    /*
        The Context passed into this should be the same one that was used to set it
        TODO: Make a single "Manage alarms activity"
        this way the context will be the same for
        set alarm and cancel alarm
     */
    public void cancelAlarm() {
        if(this.context == null) {
            return;
        }
        PendingIntent pendingIntent;

        Intent alarmIntent = new Intent(this.context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Alarm.instance = null;
    }
}
