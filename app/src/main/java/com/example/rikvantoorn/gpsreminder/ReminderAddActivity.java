/**
 * ReminderAddActivity
 * Rik van Toorn, 11279184
 *
 * This activity handles adding a reminder to the database or deleting/edititng a existing one. the user can fill in different fields.
 */

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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
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

    // declare all global variables and views
    private DatabaseReference databaseReference;

    private LatLng latlng;

    private String date;
    private String location;

    private int whenwarning;
    private int distance;

    private Button buttonToReminderList;
    private Button buttonToMap;
    private Button buttonToLogout;
    private Button buttonAddReminderAdd;
    private Button buttonAddReminderCancel;

    private TextView textViewDistance;

    private EditText editTextReminderTitle;
    private EditText editTextReminderDescription;

    private CheckBox checkBoxEnteringLocation;
    private CheckBox checkBoxLeavingLocation;

    private SeekBar seekBarDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_add);

        // get extras from the intent
        Bundle extras = getIntent().getExtras();
        String activity = extras.getString("activity");
        String title = extras.getString("title");
        String description = extras.getString("description");
        date = extras.getString("date");
        location = extras.getString("location");
        distance = extras.getInt("distance");
        whenwarning = extras.getInt("whenwarning");
        double coordinateslatitude = extras.getDouble("coordinateslatitude");
        double coordinateslongitude = extras.getDouble("coordinateslongitude");


        // set the right views
        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonToLogout = (Button) findViewById(R.id.buttonToLogout);
        buttonAddReminderAdd = (Button) findViewById(R.id.buttonAddReminderAdd);
        buttonAddReminderCancel = (Button) findViewById(R.id.buttonAddReminderCancel);

        editTextReminderTitle = (EditText) findViewById(R.id.editTextReminderTitle);
        editTextReminderDescription = (EditText) findViewById(R.id.editTextReminderDescription);

        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        seekBarDistance = (SeekBar) findViewById(R.id.seekBarDistance);

        checkBoxEnteringLocation = (CheckBox) findViewById(R.id.checkBoxEnteringLocation);
        checkBoxLeavingLocation = (CheckBox) findViewById(R.id.checkBoxLeavinglocation);

        // set onclick listeners on buttons
        buttonToReminderList.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonToLogout.setOnClickListener(this);
        buttonAddReminderAdd.setOnClickListener(this);
        buttonAddReminderCancel.setOnClickListener(this);

        // checks if user is logged in
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // set views with the right information if it is to edit a existing reminder
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
            whenwarning = 0;
        }

        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Location");

        // get title and location from the google places API
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

        // set listener on seekbar
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

    // function to add the filled in form to the database with reminders
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

        Reminder reminder = new Reminder(title, location, description, date, distance, latlng, whenwarning );

        DatabaseReference childRef = databaseReference.child("Reminders").child(title);
        childRef.setValue(reminder);

        startActivity(new Intent(getApplicationContext(), ReminderListActivity.class));
    }



    // handles the onclick listeners
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
        if(view == buttonAddReminderAdd) {
            addReminder();
        }
        if(view == buttonAddReminderCancel) {
            startActivity(new Intent(this, ReminderListActivity.class));
        }
    }
}
