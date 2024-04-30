package com.moutamid.trip4pet.models;

public class Cities {
    public String name, country, state;
    public double latitude, longitude;

    public Cities(String name, String country, String state, double latitude, double longitude) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
