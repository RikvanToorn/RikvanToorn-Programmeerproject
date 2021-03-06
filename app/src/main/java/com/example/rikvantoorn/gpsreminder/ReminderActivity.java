/**
 * ReminderActivity
 * Rik van Toorn, 11279184
 *
 * This activity shows the information for the reminder the user clicked on trough notification or listview from the ReminderListactivity.
 * from here the user can choose to edit the reminder
 */

package com.example.rikvantoorn.gpsreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener{

    // declare all global variables and views
    private DatabaseReference databaseReference;

    private Button buttonToReminderList;
    private Button buttonToMap;
    private Button buttonReminderEdit;
    private Button buttonReminderDelete;
    private Button buttonToLogout;

    private String date;
    private String title;
    private String description;
    private String location;
    private Integer distance;
    private Integer whenwarning;
    public double coordinateslatitude;
    public double coordinateslongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Bundle extras = getIntent().getExtras();

        title = extras.getString("title");
        date = extras.getString("date");
        description = extras.getString("description");
        location = extras.getString("location");
        distance = extras.getInt("distance");
        whenwarning = extras.getInt("whenwarning");
        coordinateslatitude = extras.getDouble("coordinateslatitude");
        coordinateslongitude = extras.getDouble("coordinateslongitude");


        // set the right views
        TextView textViewReminderTitle = (TextView) findViewById(R.id.textViewReminderTitle);
        TextView textViewReminderLocatie = (TextView) findViewById(R.id.textViewReminderLocatie);
        TextView textViewReminderInformation = (TextView) findViewById(R.id.textViewReminderInformation);

        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonReminderEdit = (Button) findViewById(R.id.buttonReminderEdit);
        buttonReminderDelete = (Button) findViewById(R.id.buttonReminderDelete);
        buttonToLogout = (Button) findViewById(R.id.buttonToLogout);

        CheckBox checkBoxEnteringLocation = (CheckBox) findViewById(R.id.checkBoxEnteringLocation);
        CheckBox checkBoxLeavingLocation = (CheckBox) findViewById(R.id.checkBoxLeavinglocation);

        textViewReminderTitle.setText(title);
        textViewReminderLocatie.setText(location + " - " + distance + "M");
        textViewReminderInformation.setText(description);

        if (whenwarning == 1) {
            checkBoxEnteringLocation.setChecked(true);
        }
        if (whenwarning == 2) {
            checkBoxLeavingLocation.setChecked(true);
        }
        if (whenwarning == 3) {
            checkBoxEnteringLocation.setChecked(true);
            checkBoxLeavingLocation.setChecked(true);
        }

        // set onclicklisteners
        buttonToReminderList.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonReminderEdit.setOnClickListener(this);
        buttonReminderDelete.setOnClickListener(this);
        buttonToLogout.setOnClickListener(this);

        // checks if user is logged in
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
    }

    // handles onclick listeners
    @Override
    public void onClick(View view) {
        if(view == buttonToMap) {
            startActivity(new Intent(this, MapActivity.class));
        }
        if(view == buttonToReminderList) {
            startActivity(new Intent(this, ReminderListActivity.class));
        }
        if(view == buttonToLogout) {
            startActivity(new Intent(this, LogoutActivity.class));
        }
        if(view == buttonReminderDelete) {
            DatabaseReference childRef = databaseReference.child("Reminders").child(title);
            childRef.removeValue();
            startActivity(new Intent(this, ReminderListActivity.class));
        }
        if(view == buttonReminderEdit) {
            Intent reminderAddIntent = new Intent(ReminderActivity.this, ReminderAddActivity.class);
            reminderAddIntent.putExtra("activity", "ReminderActivity");
            reminderAddIntent.putExtra("date", date);
            reminderAddIntent.putExtra("title", title);
            reminderAddIntent.putExtra("description", description);
            reminderAddIntent.putExtra("location", location);
            reminderAddIntent.putExtra("distance", distance);
            reminderAddIntent.putExtra("whenwarning", whenwarning);
            reminderAddIntent.putExtra("coordinateslatitude", coordinateslatitude);
            reminderAddIntent.putExtra("coordinateslongitude", coordinateslongitude);
            startActivity(reminderAddIntent);
        }
    }
}
