package com.moutamid.trip4pet.models;

public class Cities {
    public int id;
    public String name;
    public int state_id;
    public String state_code;
    public String state_name;
    public int country_id;
    public String country_code;
    public String country_name;
    public double latitude;
    public double longitude;
    public String wikiDataId;

    public Cities() {
    }

    public Cities(int id, String name, int state_id, String state_code, String state_name, int country_id, String country_code, String country_name, double latitude, double longitude, String wikiDataId) {
        this.id = id;
        this.name = name;
        this.state_id = state_id;
        this.state_code = state_code;
        this.state_name = state_name;
        this.country_id = country_id;
        this.country_code = country_code;
        this.country_name = country_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wikiDataId = wikiDataId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
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

    public String getWikiDataId() {
        return wikiDataId;
    }

    public void setWikiDataId(String wikiDataId) {
        this.wikiDataId = wikiDataId;
    }
}
