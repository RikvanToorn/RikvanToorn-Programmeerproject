package com.example.rikvantoorn.gpsreminder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

/**
 * Created by Rik van Toorn on 23-1-2017.
 */

public class AsyncTaskNotification extends AsyncTask<Void, Void, Void> {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onPreExecute() {
        Log.d("ja hoor", "ja hoor");
        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());


        Log.d("ja hoor", user.getUid());
    }

    @Override
    protected Void doInBackground(Void... params) {
        DatabaseReference childref = databaseReference.child("Reminders");
        childref.addChildEventListener(new ChildEventListener() {
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

                Log.d("ja hoor", title);

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
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
