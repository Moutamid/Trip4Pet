package com.moutamid.trip4pet.models;

public class FilterModel {
    public String name;
    public int icon, id;

    public FilterModel() {
    }

    public FilterModel(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
}
