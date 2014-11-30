package com.userinterfaces.ontime.Model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.userinterfaces.ontime.AddAlarm;
import com.userinterfaces.ontime.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by AKresling on 14-11-30.
 */
public class Alarm {
    private int alarmID;
    private int hour;
    private int minute;
    private int year;
    private int day;
    private int month;
    private String alarmName;
    private Context context;

    public Alarm(int alarmID, int hour, int minute, int year, int day, int month, String alarmName) {
        this.alarmID = alarmID;
        this.hour = hour;
        this.minute = minute;
        this.year = year;
        this.day = day;
        this.month = month;
        this.alarmName = alarmName;
    }

    public int getAlarmID() {
        return alarmID;
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
        cal.set(this.year, this.month, this.day, this.hour, this.minute);
        return cal.getTimeInMillis();
    }

    public static Alarm createNewAlarm(
            int hour,
            int minute,
            int year,
            int day,
            int month,
            String alarmName
    ) {
        /*
            Need a way to come up with new id
            Probably by retrieving lastest id from DB
         */
        int id = 0;
        return new Alarm(id, hour, minute, year, day, month, alarmName);
    }

    public void setAlarm(Context context) {
        this.context = context;
        PendingIntent pendingIntent;

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, this.timeInMillis(), pendingIntent);
    }

    /*
        The Context passed into this should be the same one that was used to set it
        TODO: Make a single "Manage alarms activity"
        this way the context will be the same for
        set alarm and cancel alarm
     */
    public void cancelAlarm(Context context) {
        this.context = context;
        PendingIntent pendingIntent;

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
