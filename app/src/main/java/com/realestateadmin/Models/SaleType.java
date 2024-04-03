package com.realestateadmin.Models;

public class SaleType {
    private String id;
    private String type;

    public SaleType() {
        // Default constructor required for Firebase
    }

    public SaleType(String id, String type) {
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
