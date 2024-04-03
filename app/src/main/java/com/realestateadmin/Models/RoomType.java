package com.realestateadmin.Models;

public class RoomType {
    private String id;
    private String type;

    public RoomType() {
        // Default constructor required for Firebase
    }

    public RoomType(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
