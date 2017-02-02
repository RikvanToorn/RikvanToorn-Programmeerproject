/**
 * Reminder class
 * Rik van Toorn, 11279184
 *
 * This class is a blueprint for the Reminder object which will be stored and read from the fire database.
 */

package com.example.rikvantoorn.gpsreminder;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Rik van Toorn on 16-1-2017.
 */

public class Reminder {
    public String title;
    public String location;
    public String description;
    public String date;

    public int whenwarning;
    public int distance;
    public LatLng coordinates;


    public Reminder(String title, String location, String description, String date, Integer distance,  LatLng coordinates, Integer whenwarning) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.date = date;

        this.distance = distance;
        this.whenwarning = whenwarning;

        this.coordinates = coordinates;
    }

    public String gettitle() {
        return title;
    }

    public String getlocation() {
        return location;
    }

    public String getdescription() {
        return description;
    }

    public String getdate() {
        return date;
    }

    public Integer getdistance() { return distance; }

    public Integer getwhenwarning() {
        return whenwarning;
    }

    public LatLng getcoordinates() { return coordinates; }
}
