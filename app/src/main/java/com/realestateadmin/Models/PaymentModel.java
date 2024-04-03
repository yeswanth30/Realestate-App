package com.realestateadmin.Models;

public class PaymentModel {
    private String borkerid;
    private String broker_name;
    private String imageurl;
    private String payment_id;
    private String propertyId;
    private String propertyName;
    private String timestamp;
    private String total_amount;
    private String  username;


    public PaymentModel() {
        // Default constructor required for calls to DataSnapshot.getValue(PaymentModel.class)
    }

    public PaymentModel(String borkerid, String broker_name, String imageurl, String payment_id,
                        String propertyId, String propertyName, String timestamp, String total_amount, String username) {
        this.borkerid = borkerid;
        this.broker_name = broker_name;
        this.imageurl = imageurl;
        this.payment_id = payment_id;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.timestamp = timestamp;
        this.total_amount = total_amount;
        this.username = username;
    }

    public String getBorkerid() {
        return borkerid;
    }

    public void setBorkerid(String borkerid) {
        this.borkerid = borkerid;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
