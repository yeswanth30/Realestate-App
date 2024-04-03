package com.realestateadmin.Models;

public class PropertyType {
    private String id;
    private String type;
    private String imageUrl; // Add this field for image URL

    public PropertyType() {
        // Default constructor required for Firebase
    }

    public PropertyType(String id, String type, String imageUrl) {
        this.id = id;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
