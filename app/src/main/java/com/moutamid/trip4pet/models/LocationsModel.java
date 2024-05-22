package com.moutamid.trip4pet.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LocationsModel {
    public String id, userID, name, country, city, description, contact, typeOfPlace, address;
    public double latitude, longitude;
    public ArrayList<String> images;
    public ArrayList<FilterModel> activities;
    public ArrayList<FilterModel> services;
    public long timestamp;
    public double rating;
    public boolean isAccessibleToAnimals;
    public ArrayList<CommentModel> comments;

    public LocationsModel() {
    }


}
