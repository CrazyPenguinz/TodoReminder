package com.example.todolist.Model;

import androidx.room.Entity;

@Entity
public class Location {
    private String placeName;
    private double latitude;
    private double longitude;

    public Location(String placeName, double latitude, double longitude) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
