package com.example.rikvantoorn.gpsreminder;

import android.location.Location;

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

/**
 * Created by Rik van Toorn on 25-1-2017.
 */

public class GpsHelper {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private DatabaseReference childref;

    private Reminder reminder;

    final List<Reminder> Reminders = new ArrayList<>();

    public void start() {

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
        childref = databaseReference.child("Reminders");

    }

    public String checkGps(Double latitude, Double longitude) {

        String result = "no result";
        for (Reminder reminder: Reminders) {

            float[] results = new float[3];

            String title = reminder.gettitle();
            LatLng latlng = reminder.getcoordinates();
            Double rLatitude = latlng.latitude;
            Double rLongitude = latlng.longitude;
            Integer distance = reminder.getdistance();


            Location.distanceBetween(latitude, longitude, rLatitude, rLongitude, results);
            int truedistance = Math.round(results[0]);

            if (truedistance <= distance) {
                result = title;
            }
        }
        return result;
    }



    public void getData() {

        childref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> mapStrings = (Map) dataSnapshot.getValue();
                Map<String, Long> mapLongs = (Map) dataSnapshot.getValue();
                Map<String, Map> mapLatLng = (Map) dataSnapshot.getValue();

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
    }

}
