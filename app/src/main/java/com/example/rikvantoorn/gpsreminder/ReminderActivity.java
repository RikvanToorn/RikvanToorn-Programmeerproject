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

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private Button buttonToReminderList;
    private Button buttonToMap;
    private Button buttonReminderEdit;
    private Button buttonReminderDelete;

    private TextView textViewReminderTitle;
    private TextView textViewReminderLocatie;
    private TextView textViewReminderInformation;

    private CheckBox checkBoxEnteringLocation;
    private CheckBox checkBoxLeavingLocation;


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
        date = extras.getString("date");
        title = extras.getString("title");
        description = extras.getString("description");
        location = extras.getString("location");
        distance = extras.getInt("distance");
        whenwarning = extras.getInt("whenwarning");
        coordinateslatitude = extras.getDouble("coordinateslatitude");
        coordinateslongitude = extras.getDouble("coordinateslongitude");



        textViewReminderTitle = (TextView) findViewById(R.id.textViewReminderTitle);
        textViewReminderLocatie = (TextView) findViewById(R.id.textViewReminderLocatie);
        textViewReminderInformation = (TextView) findViewById(R.id.textViewReminderInformation);

        checkBoxEnteringLocation = (CheckBox) findViewById(R.id.checkBoxEnteringLocation);
        checkBoxLeavingLocation = (CheckBox) findViewById(R.id.checkBoxLeavinglocation);


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


        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonReminderEdit = (Button) findViewById(R.id.buttonReminderEdit);
        buttonReminderDelete = (Button) findViewById(R.id.buttonReminderDelete);

        buttonToReminderList.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonReminderEdit.setOnClickListener(this);
        buttonReminderDelete.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
    }

    @Override
    public void onClick(View view) {
        if(view == buttonToMap) {
            startActivity(new Intent(this, MapActivity.class));
        }
        if(view == buttonToReminderList) {
            startActivity(new Intent(this, ReminderListActivity.class));
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
