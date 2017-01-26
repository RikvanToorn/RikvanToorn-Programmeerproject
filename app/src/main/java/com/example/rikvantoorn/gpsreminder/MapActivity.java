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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    private Button buttonToReminderList;
    private Button buttonToReminder;

    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        buttonToReminderList = (Button) findViewById(R.id.buttonToReminderList);
        buttonToReminder = (Button) findViewById(R.id.buttonToReminder);

        buttonToReminderList.setOnClickListener(this);
        buttonToReminder.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();




//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//                requestPermissions(new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.INTERNET
//                }, 10);
//
//                return;
//            }
//        } else {
//            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
//        }
//        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 10:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
//                return;
//        }
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(52.354679, 4.955758);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Science Park"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View view) {
        if(view == buttonToReminder) {
            startActivity(new Intent(this, ReminderActivity.class));
        }
        if(view == buttonToReminderList) {
            startActivity(new Intent(this, ReminderListActivity.class));
        }
    }
}
