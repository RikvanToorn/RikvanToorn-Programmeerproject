package com.example.rikvantoorn.gpsreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class ReminderAddActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private Reminder reminder;

    private LatLng latlng;

    private String activity;
    private String date;
    private String location;
    private String description;
    private String title;

    private double coordinateslatitude;
    private double coordinateslongitude;

    private int whenwarning;
    private int distance;

    private Button buttonToReminderList;
    private Button buttonToMap;
    private Button buttonToReminder;
    private Button buttonAddReminderAdd;

    private TextView textViewDistance;

    private EditText editTextReminderTitle;
    private EditText editTextReminderDescription;

    private CheckBox checkBoxEnteringLocation;
    private CheckBox checkBoxLeavingLocation;

    private SeekBar seekBarDistance;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_add);

        Bundle extras = getIntent().getExtras();
        activity = extras.getString("activity");
        date = extras.getString("date");
        title = extras.getString("title");
        description = extras.getString("description");
        location = extras.getString("location");
        distance = extras.getInt("distance");
        whenwarning = extras.getInt("whenwarning");
        coordinateslatitude = extras.getDouble("coordinateslatitude");
        coordinateslongitude = extras.getDouble("coordinateslongitude");

        Intent intent = new Intent(this,GpsService.class);
        stopService(intent);


        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonToReminder = (Button) findViewById(R.id.buttonToReminder);
        buttonAddReminderAdd = (Button) findViewById(R.id.buttonAddReminderAdd);

        editTextReminderTitle = (EditText) findViewById(R.id.editTextReminderTitle);
        editTextReminderDescription = (EditText) findViewById(R.id.editTextReminderDescription);

        buttonToReminderList.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonToReminder.setOnClickListener(this);
        buttonAddReminderAdd.setOnClickListener(this);

        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        seekBarDistance = (SeekBar) findViewById(R.id.seekBarDistance);

        checkBoxEnteringLocation = (CheckBox) findViewById(R.id.checkBoxEnteringLocation);
        checkBoxLeavingLocation = (CheckBox) findViewById(R.id.checkBoxLeavinglocation);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                //.enableAutoManage(this, this)
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);



        if (activity.equals("ReminderActivity")) {
            editTextReminderTitle.setText(title);
            editTextReminderDescription.setText(description);
            textViewDistance.setText(distance + "m");
            seekBarDistance.setProgress(distance);
            buttonAddReminderAdd.setText("Edit");
            ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint(location);
            latlng = new LatLng(coordinateslatitude, coordinateslongitude);
            Log.d("ja", "jaja" + latlng);
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
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latlng = place.getLatLng();
                location = "" + place.getName();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                textViewDistance.setText(String.valueOf(progress) + "M");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void addReminder() {
        String title = editTextReminderTitle.getText().toString().trim();
        String description = editTextReminderDescription.getText().toString().trim();
        distance = seekBarDistance.getProgress();
        date = DateFormat.getDateTimeInstance().format(new Date());

        if(TextUtils.isEmpty(location)){
            Toast.makeText(this, "Please enter an location", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Please enter an title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (distance == 0) {
            Toast.makeText(this, "Please enter a distance higher then 0M", Toast.LENGTH_SHORT).show();
            return;
        }

        if (( checkBoxEnteringLocation).isChecked()) {
            whenwarning = whenwarning + 1;
        }

        if (( checkBoxLeavingLocation).isChecked()) {
            whenwarning = whenwarning + 2;
        }

        if (whenwarning == 0) {
            Toast.makeText(this, "Please check one or both options", Toast.LENGTH_SHORT).show();
            return;
        }

        reminder = new Reminder(title, location, description, date, distance, latlng, whenwarning );

        DatabaseReference childRef = databaseReference.child("Reminders").child(title);
        childRef.setValue(reminder);

        startActivity(new Intent(getApplicationContext(), ReminderListActivity.class));
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
        if(view == buttonAddReminderAdd) {
            addReminder();
        }
    }
}
