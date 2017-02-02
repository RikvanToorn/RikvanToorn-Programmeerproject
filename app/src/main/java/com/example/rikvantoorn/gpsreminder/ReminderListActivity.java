/**
 * ReminderAddActivity
 * Rik van Toorn, 11279184
 *
 * This is the activity which is shown when de user logges in or starts the app after closing it.
 * from here the user can add and see existing reminders to edit or delete
 */

package com.example.rikvantoorn.gpsreminder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReminderListActivity extends AppCompatActivity implements View.OnClickListener{

    // declare all global variables and views
    private Button buttonAddReminder;
    private Button buttonToMap;
    private Button buttonToLogout;

    private Reminder reminder;

    final List<Reminder> Reminders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        // set the right views
        buttonAddReminder = (Button) findViewById(R.id.buttonAddReminder);
        buttonToMap = (Button) findViewById(R.id.buttonToMap);
        buttonToLogout = (Button) findViewById(R.id.buttonToLogout);

        // attach onclick listeners
        buttonAddReminder.setOnClickListener(this);
        buttonToMap.setOnClickListener(this);
        buttonToLogout.setOnClickListener(this);

        // Checks if user is logged in
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // set custom adapter for listview
        final ListView addedReminders = (ListView) findViewById(R.id.listViewReminders);
        final ArtistAdapter adapter = new ArtistAdapter(getApplicationContext(), R.layout.reminder_row, Reminders);
        addedReminders.setAdapter(adapter);


        // get all reminders from database
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid()).child("Reminders");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> mapStrings = (Map)dataSnapshot.getValue();
                Map<String, Long> mapLongs= (Map)dataSnapshot.getValue();
                Map<String, Map> mapLatLng= (Map)dataSnapshot.getValue();

                String title = mapStrings.get("title");
                String location = mapStrings.get("location");
                String date = mapStrings.get("date");
                String description = mapStrings.get("description");

                Long distancelong = mapLongs.get("distance");
                Long whenwarninglong = mapLongs.get("whenwarning");
                Integer distance = distancelong.intValue();
                Integer whenwarning = whenwarninglong.intValue();

                Map<String, Double> coordinatesmap = mapLatLng.get("coordinates");

                Double longitude = coordinatesmap.get("longitude");
                Double latitude = coordinatesmap.get("latitude");

                LatLng coordinates = new LatLng(latitude, longitude);

                reminder = new Reminder(title, location, description, date, distance, coordinates, whenwarning);
                Reminders.add(reminder);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // set onclick listener to each listview item
        addedReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String date = Reminders.get(position).getdate();
                String title = Reminders.get(position).gettitle();
                String description = Reminders.get(position).getdescription();
                String location = Reminders.get(position).getlocation();
                Integer distance = Reminders.get(position).getdistance();
                Integer whenwarning = Reminders.get(position).getwhenwarning();
                LatLng coordinates = Reminders.get(position).getcoordinates();

                double coordinateslongitude = coordinates.longitude;
                double coordinateslatitude = coordinates.latitude;

                Intent reminderIntent = new Intent(ReminderListActivity.this, ReminderActivity.class);
                reminderIntent.putExtra("date", date);
                reminderIntent.putExtra("title", title);
                reminderIntent.putExtra("description", description);
                reminderIntent.putExtra("location", location);
                reminderIntent.putExtra("distance", distance);
                reminderIntent.putExtra("whenwarning", whenwarning);
                reminderIntent.putExtra("coordinateslongitude", coordinateslongitude);
                reminderIntent.putExtra("coordinateslatitude", coordinateslatitude);
                startActivity(reminderIntent);
            }
        });

        // aks user to grant permission if not yet given
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }
        Intent intent = new Intent(this,GpsService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
        }
    }

    // handles the onclick listeners
    @Override
    public void onClick(View view) {
        if(view == buttonToMap) {
            startActivity(new Intent(this, MapActivity.class));
        }
        if(view == buttonAddReminder) {
            Intent reminderAddIntent = new Intent(ReminderListActivity.this, ReminderAddActivity.class);
            reminderAddIntent.putExtra("activity", "ReminderListActivity");
            startActivity(reminderAddIntent);
        }
        if(view == buttonToLogout) {
            startActivity(new Intent(this, LogoutActivity.class));
        }
    }

    // the custom adapter to populate the textviews
    public class ArtistAdapter extends ArrayAdapter {

        private List<Reminder> ReminderList;
        private int resource;
        private LayoutInflater inflater;
        public ArtistAdapter(Context context, int resource, List<Reminder> objects) {
            super(context, resource, objects);
            ReminderList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource, null);
            }

            TextView dateTextViewRow;
            TextView locationTextViewRow;
            TextView titleTextViewRow;

            dateTextViewRow = (TextView) convertView.findViewById(R.id.dateTextViewRow);
            locationTextViewRow = (TextView) convertView.findViewById(R.id.locationTextViewRow);
            titleTextViewRow = (TextView) convertView.findViewById(R.id.titleTextViewRow);

            dateTextViewRow.setText(ReminderList.get(position).getdate());
            locationTextViewRow.setText(ReminderList.get(position).getlocation() + " - " + ReminderList.get(position).getdistance() + "m");
            titleTextViewRow.setText(ReminderList.get(position).gettitle());
            return convertView;
        }
    }
}
