package com.example.rikvantoorn.gpsreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReminderAddActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonToReminderList;
    private Button buttonToMap;
    private Button buttonToReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_add);

        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonToReminder = (Button) findViewById(R.id.buttonToReminder);

        buttonToReminderList.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonToReminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonToMap) {
            startActivity(new Intent(this, MapActivity.class));
        }
        if(view == buttonToReminderList) {
            startActivity(new Intent(this, ReminderListActivity.class));
        }
        if(view == buttonToReminder) {
            startActivity(new Intent(this, ReminderActivity.class));
        }
    }
}
