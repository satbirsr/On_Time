package com.userinterfaces.ontime;

import android.app.Activity;
import android.app.AlarmManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.userinterfaces.ontime.Model.Alarm;

public class AddAlarm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button setButton = (Button) findViewById(R.id.setButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        setButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Alarm alarm = Alarm.getInstance(
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute(),
                        datePicker.getYear(),
                        datePicker.getDayOfMonth(),
                        datePicker.getMonth(),
                        "New Alarm"
                );
                alarm.setAlarm(AddAlarm.this);

                System.out.println("Alarm set for " + alarm.getDay() + " " + alarm.getHour() + " " + alarm.getMinute());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Alarm alarm = Alarm.getExistingInstance();

                alarm.cancelAlarm();

                System.out.println("Alarm set for " + alarm.getDay() + " " + alarm.getHour() + " " + alarm.getMinute());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
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
}
