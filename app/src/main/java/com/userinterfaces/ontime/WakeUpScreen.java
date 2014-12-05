package com.userinterfaces.ontime;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WakeUpScreen extends Activity {

    // constants
    int UPDATE_INTERVAL = 1000;     // duration (ms) to sleep between updates to time displays

    // time formatting
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");
    SimpleDateFormat markerFormat = new SimpleDateFormat("a");
    SimpleDateFormat secFormat = new SimpleDateFormat("ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d");

    private MediaPlayer alarmSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wake_up_screen);

        System.out.println("Waking up device");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "OnTime");
        wakeLock.acquire();

        // check weather condition
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.alarmData), Context.MODE_PRIVATE);
        String displayCondition = "The weather's fine.";
        if (sharedpreferences.contains(getString(R.string.weatherCondition))) {
            String rawCondition = sharedpreferences.getString(getString(R.string.weatherCondition), "");
            if (rawCondition.length() > 0) {
                if (rawCondition.equals("SNOWY"))
                    displayCondition = "It's snowy today.";
                else if (rawCondition.equals("CLOUDY"))
                    displayCondition = "It's cloudy today.";
                else if (rawCondition.equals("RAINY"))
                    displayCondition = "It's rainy today.";
            }
        }
        TextView weatherConditionText = (TextView)findViewById(R.id.weatherCondition);
        weatherConditionText.setText(displayCondition);

        Button imAwakeButton = (Button) findViewById(R.id.imAwakeButton);

        imAwakeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismissAlarm();
            }
        });

        playSound(this, getAlarmUri());

        // start updating the time
        Runnable updater = new TimeUpdater();
        Thread updateThread = new Thread(updater);
        updateThread.start();

        // prevent initiation of extra alarm sounds by locking orientation
        lockOrientation();
    }

    private void playSound(Context context, Uri alert) {
        alarmSound = new MediaPlayer();
        try {
            alarmSound.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                alarmSound.setAudioStreamType(AudioManager.STREAM_ALARM);
                alarmSound.prepare();
                alarmSound.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wake_up_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateTime() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    // update main time display
                    Calendar c = Calendar.getInstance();
                    String time = timeFormat.format(c.getTime());
                    String marker = markerFormat.format(c.getTime());
                    String sec = secFormat.format(c.getTime());
                    String date = dateFormat.format(c.getTime());

                    TextView timeText = (TextView)findViewById(R.id.theTime);
                    TextView markerText = (TextView)findViewById(R.id.theMarker);
                    TextView secText = (TextView)findViewById(R.id.theSec);
                    TextView dateText = (TextView)findViewById(R.id.theDate);

                    timeText.setText(time);
                    markerText.setText(marker);
                    secText.setText(sec);
                    dateText.setText(date);

                } catch (Exception e) {
                    // do nothing
                }
            }
        });
    }

    class TimeUpdater implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    updateTime();
                    Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }

    private void dismissAlarm () {
        alarmSound.stop();

        // clear next alarm time
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.alarmData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(getString(R.string.nextAlarm), getString(R.string.noNextAlarm));
        editor.commit();

        Intent homeScreen = new Intent(getApplicationContext(), Home.class);
        startActivity(homeScreen);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissAlarm();
    }


    // Based on a method by Roy of StackOverflow.com
    private void lockOrientation() {
        Display display = this.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            height = display.getHeight();
            width = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
            width = size.x;
        }
        switch (rotation) {
            case Surface.ROTATION_90:
                if (width > height)
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
            case Surface.ROTATION_180:
                if (height > width)
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                else
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case Surface.ROTATION_270:
                if (width > height)
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                else
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default :
                if (height > width)
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
