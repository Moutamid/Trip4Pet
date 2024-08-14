package com.moutamid.trip4pet.models;

import com.google.gson.annotations.SerializedName;

public class Cities {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("state_code")
    private String state_code;

    @SerializedName("state_name")
    private String state_name;

    @SerializedName("country_id")
    private int country_id;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("country_name")
    private String country_name;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("wikiDataId")
    private String wikiDataId;

    public Cities() {
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getWikiDataId() {
        return wikiDataId;
    }

    public void setWikiDataId(String wikiDataId) {
        this.wikiDataId = wikiDataId;
    }

    @Override
    public String toString() {
        return "Cities{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state_id=" + state_id +
                ", state_code='" + state_code + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_id=" + country_id +
                ", country_code='" + country_code + '\'' +
                ", country_name='" + country_name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", wikiDataId='" + wikiDataId + '\'' +
                '}';
    }
}
