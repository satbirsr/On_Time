package com.userinterfaces.ontime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Home extends Activity {

    // constants
    int UPDATE_INTERVAL = 1000;     // duration (ms) to sleep between updates to time displays

    // default time formatting
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");
    SimpleDateFormat markerFormat = new SimpleDateFormat("a");
    SimpleDateFormat secFormat = new SimpleDateFormat("ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);

        // start updating the time
        Runnable updater = new TimeUpdater();
        Thread updateThread = new Thread(updater);
        updateThread.start();

        Button goToAddAlarmButton = (Button) findViewById(R.id.goToAddAlarmButton);

        goToAddAlarmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), AddAlarm.class);

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
}
