package com.moutamid.trip4pet.models;

import com.google.gson.annotations.SerializedName;

public class Cities {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("state_name")
    private String state_name;

    @SerializedName("country_name")
    private String country_name;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public Cities() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Cities{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
