package com.moutamid.trip4pet.models;

import java.util.ArrayList;

public class LocationsModel {
    public String name, country, city, description, parkingCost, contact, typeOfPlace;
    public double latitude, longitude;
    public ArrayList<String> images;
    public ArrayList<Integer> activities;
    public ArrayList<Integer> services;
    public long timestamp;
    public int rating;

    public LocationsModel(String name, String country, String city, String description, String parkingCost, String contact, String typeOfPlace, double latitude, double longitude, ArrayList<String> images, ArrayList<Integer> activities, ArrayList<Integer> services, long timestamp, int rating) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.description = description;
        this.parkingCost = parkingCost;
        this.contact = contact;
        this.typeOfPlace = typeOfPlace;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.activities = activities;
        this.services = services;
        this.timestamp = timestamp;
        this.rating = rating;
    }
}
