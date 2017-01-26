package com.example.rikvantoorn.gpsreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Rik van Toorn on 26-1-2017.
 */

public class GpsService extends Service{

    private GpsHelper gpshelper;
    private LocationManager manager;
    private LocationListener listener;

    private String result;

    private NotificationManager notificationManager;

    boolean isNotificActive = false;


    final class GpsThreadClass implements Runnable{
        int service_id;
        GpsThreadClass(int service_id) {
            this.service_id = service_id;
        }

        @Override
        public void run() {


        }
    }

    public void showNotification( String title) {

        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(GpsService.this)
                .setContentTitle("Gps Reminder")
                .setContentText(title)
                .setTicker("Don't forget to: " + title + "!")
                .setSmallIcon(R.drawable.reminder_icon);

        Intent showReminderIntent = new Intent(this, ReminderActivity.class);

        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        tStackBuilder.addParentStack(ReminderActivity.class);

        tStackBuilder.addNextIntent(showReminderIntent);

        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(10, notificBuilder.build());

        isNotificActive = true;

    }

    @Override
    public void onCreate() {
        gpshelper = new GpsHelper();
        gpshelper.start();
        gpshelper.getData();

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);



        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                result = gpshelper.checkGps(latitude, longitude);


                if (!(result.equals("no result"))) {
                    Log.d("result: ", result);

                    showNotification(result);

//                    Intent i = new Intent();
//                    i.setClass(GpsService.this, MapActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
                }
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("testem", "je doet hem aan");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Go to 'map' to grant permissions", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "jajaja", Toast.LENGTH_SHORT).show();
        Thread thread = new Thread(new GpsThreadClass(startId));
        thread.start();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
