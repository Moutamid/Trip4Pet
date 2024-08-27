package com.moutamid.trip4pet.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LocationsModel {
    public String id, userID, name, country, city, description, contact, typeOfPlace, address, created_by, price;
    public int placeID;
    public double latitude, longitude;
    public ArrayList<String> images;
    public ArrayList<FilterModel> services;
    public long timestamp;
    public long opening_time, closing_time, opening_time_evening, closing_time_evening, lunch_time_morning, lunch_time_evening;
    public double rating;
    public boolean isAccessibleToAnimals;
    public boolean isAlwaysOpen, hasShift;
    public ArrayList<CommentModel> comments;

    public LocationsModel() {
    }

}
