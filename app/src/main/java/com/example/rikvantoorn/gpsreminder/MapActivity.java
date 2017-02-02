/**
 * MapActivity
 * Rik van Toorn, 11279184
 *
 * In the MapActivity the user can check where he is and see nearby reminders.
 */

package com.example.rikvantoorn.gpsreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    private Button buttonToReminderList;
    private Button buttonToLogout;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference childref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToLogout = (Button) findViewById(R.id.buttonToLogout);

        buttonToReminderList.setOnClickListener(this);
        buttonToLogout.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
        childref = databaseReference.child("Reminders");

        childref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Map> mapLatLng= (Map)dataSnapshot.getValue();
                Map<String, String> mapStrings = (Map)dataSnapshot.getValue();
                Map<String, Long> mapLongs= (Map)dataSnapshot.getValue();

                Long distancelong = mapLongs.get("distance");
                Map<String, Double> coordinatesmap = mapLatLng.get("coordinates");

                Double longitude = coordinatesmap.get("longitude");
                Double latitude = coordinatesmap.get("latitude");

                LatLng coordinates = new LatLng(latitude, longitude);
                String title = mapStrings.get("title");

                mMap.addMarker(new MarkerOptions().position(coordinates).title(title));

                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(coordinates)
                        .radius(distancelong));

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




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng userPosition = new LatLng(GpsService.latitude  , GpsService.longitude);
        mMap.addMarker(new MarkerOptions().position(userPosition).title("YOU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition,15));
    }

    @Override
    public void onClick(View view) {
        if(view == buttonToReminderList) {
            startActivity(new Intent(this, ReminderListActivity.class));
        }
        if(view == buttonToLogout) {
            startActivity(new Intent(this, LogoutActivity.class));
        }
    }
}
