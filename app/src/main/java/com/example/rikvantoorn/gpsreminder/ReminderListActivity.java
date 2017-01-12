package com.example.rikvantoorn.gpsreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReminderListActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonAddReminder;
    private Button buttonToMap;
    private Button buttonToReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        buttonAddReminder = (Button) findViewById(R.id.buttonAddReminder);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonToReminder = (Button) findViewById(R.id.buttonToReminder);

        buttonAddReminder.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonToReminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonToMap) {
            startActivity(new Intent(this, MapActivity.class));
        }
        if(view == buttonAddReminder) {
            startActivity(new Intent(this, ReminderAddActivity.class));
        }
        if(view == buttonToReminder) {
            startActivity(new Intent(this, ReminderActivity.class));
        }
    }
}
