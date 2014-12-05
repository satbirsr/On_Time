package com.userinterfaces.ontime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Night extends Activity {

    // constants
    int UPDATE_INTERVAL = 1000;     // duration (ms) to sleep between updates to time displays

    // default time formatting
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");
    SimpleDateFormat markerFormat = new SimpleDateFormat("a");
    SimpleDateFormat secFormat = new SimpleDateFormat("ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_night);

        // hide the OS notification bar at top of screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the OS soft-key bar at bottom of screen
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        // check next alarm time
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.alarmData), Context.MODE_PRIVATE);
        if (sharedpreferences.contains(getString(R.string.nextAlarm))) {
            TextView nextAlarmText = (TextView)findViewById(R.id.nextAlarmDisplay);
            String s = sharedpreferences.getString(getString(R.string.nextAlarm), getString(R.string.noNextAlarm));
            nextAlarmText.setText(s);
        }

        // start updating the time
        Runnable updater = new TimeUpdater();
        Thread updateThread = new Thread(updater);
        updateThread.start();

        Button goToHomeButton = (Button) findViewById(R.id.goToHomeButton);

        goToHomeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), Home.class);
                startActivity(nextScreen);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

                    // update next alarm time display
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        String value = extras.getString("nextAlarmTime");
                        if (value != null) {
                            TextView alarmText = (TextView)findViewById(R.id.nextAlarmDisplay);
                            alarmText.setText(value);
                        } else {
                            TextView alarmText = (TextView)findViewById(R.id.nextAlarmDisplay);
                            alarmText.setText(R.string.noNextAlarm);
                        }
                    }

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
}
