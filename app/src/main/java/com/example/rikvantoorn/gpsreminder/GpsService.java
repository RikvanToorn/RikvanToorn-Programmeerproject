/**
 * GpsService
 * Rik van Toorn, 11279184
 *
 * This service handles all the notifications in the background when the app is running, in the background or closed by the user.
 * It detects when the GPS location has changed and checks with the reminder in the databse if a notification needs to be send.
 */

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

import com.google.android.gms.maps.model.LatLng;


public class GpsService extends Service{

    // declare all global variables
    private GpsHelper gpshelper;

    boolean isNotificActive = false;

    public Reminder reminder;
    public Reminder reminderHolder;

    public LatLng resultCoordinates;
    public static Double latitude = 52.354528;
    public static Double longitude = 4.955317;

    public String resultTitle;
    public String resultLocation;
    public String resultDescription;
    public String resultDate;
    private String lastResult;

    public Integer resultWhenWarning;
    public Integer resultDistance;

    // creates another thread so the app keeps working
    final class GpsThreadClass implements Runnable{
        int service_id;
        GpsThreadClass(int service_id) {
            this.service_id = service_id;
        }

        @Override
        public void run() {
        }
    }

    // creates the service
    @Override
    public void onCreate() {
        gpshelper = new GpsHelper();
        gpshelper.getData();
        lastResult = "no result";

        // handles the action whenever the Gps location changes
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                reminder = gpshelper.checkGps(latitude, longitude);

                if (reminder != null) {

                    resultTitle = reminder.gettitle();
                    resultWhenWarning = reminder.getwhenwarning();
                    resultLocation= reminder.getlocation();
                    resultDescription = reminder.getdescription();
                    resultDate = reminder.getdate();
                    resultDistance = reminder.getdistance();
                    resultCoordinates = reminder.getcoordinates();


                    if (!(resultTitle.equals("no result")) && !(resultTitle.equals(lastResult)) && resultWhenWarning.equals(1)) {
                        lastResult = resultTitle;
                        showNotification(resultTitle, resultLocation, resultDescription, resultDate, resultDistance, resultWhenWarning, resultCoordinates);
                    } else if (!(resultTitle.equals("no result")) && !(resultTitle.equals(lastResult)) && resultWhenWarning.equals(2)) {
                        lastResult = resultTitle;
                        reminderHolder = reminder;
                    } else if (!(resultTitle.equals("no result")) && !(resultTitle.equals(lastResult)) && resultWhenWarning.equals(3)) {
                        lastResult = resultTitle;
                        reminderHolder = reminder;
                        showNotification(resultTitle, resultLocation, resultDescription, resultDate, resultDistance, resultWhenWarning, resultCoordinates);
                    }
                } else if (reminderHolder != null) {
                    resultTitle = reminderHolder.gettitle();
                    resultWhenWarning = reminderHolder.getwhenwarning();
                    resultLocation= reminderHolder.getlocation();
                    resultDescription = reminderHolder.getdescription();
                    resultDate = reminderHolder.getdate();
                    resultDistance = reminderHolder.getdistance();
                    resultCoordinates = reminderHolder.getcoordinates();
                    showNotification(resultTitle, resultLocation, resultDescription, resultDate, resultDistance, resultWhenWarning, resultCoordinates);
                    reminderHolder = null;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }

            // when the gps ont eh phone is disablled brings up the settingsscreen to enable it
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        // checks if the user has given permission and triggers the function to handle it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        } else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new GpsThreadClass(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // builds and handles the intent of the notification send to the phone
    public void showNotification(String title, String location, String description, String date, Integer distance, Integer whenwarning, LatLng coordinates) {

        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(GpsService.this)
                .setContentTitle(title)
                .setContentText("Gps Reminder")
                .setTicker("Don't forget to: " + title + "!")
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSmallIcon(R.drawable.reminder_icon);

        double coordinateslongitude = coordinates.longitude;
        double coordinateslatitude = coordinates.latitude;

        Intent showReminderIntent = new Intent(this, ReminderActivity.class);
        showReminderIntent.putExtra("title", title);
        showReminderIntent.putExtra("location", location);
        showReminderIntent.putExtra("description", description);
        showReminderIntent.putExtra("date", date);
        showReminderIntent.putExtra("distance", distance);
        showReminderIntent.putExtra("whenwarning", whenwarning);
        showReminderIntent.putExtra("scoordinateslongitude", coordinateslongitude);
        showReminderIntent.putExtra("coordinateslatitude", coordinateslatitude);

        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        tStackBuilder.addParentStack(ReminderListActivity.class);

        tStackBuilder.addNextIntent(showReminderIntent);

        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(10, notificBuilder.build());

        isNotificActive = true;
    }
}
