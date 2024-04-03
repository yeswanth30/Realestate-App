package com.realestateadmin.Models;

import androidx.annotation.NonNull;

public class City {
    private String id;
    private String name;

    public City() {
        // Default constructor required for calls to DataSnapshot.getValue(City.class)
    }

    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
