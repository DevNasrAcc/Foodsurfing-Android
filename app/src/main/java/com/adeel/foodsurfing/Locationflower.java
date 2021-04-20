package com.adeel.foodsurfing;

import android.app.Activity;


/**
 * Created on 15/01/16.
 */
public class Locationflower {
    private String location_id,location_address,location_image,location_price,location_name,location_ditance,location_logo, location_pickup, location_postal, location_availability;
    private Activity context;

    public Locationflower() {
    }
    public Locationflower(Activity context, String location_id,String location_name, String location_address, String location_image, String location_price,String location_ditance,String location_logo, String location_pickup, String location_postal, String location_availability) {
        this.context = context;
        this.location_address = location_address;
        this.location_name = location_name;
        this.location_id = location_id;
        this.location_image = location_image;
        this.location_price = location_price;
        this.location_pickup = location_pickup;
        this.location_ditance = location_ditance;
        this.location_logo = location_logo;
        this.location_postal = location_postal;
        this.location_availability = location_availability;
    }

    public String getLocation_pickup() {
        return location_pickup;
    }

    public void setLocation_pickup(String location_pickup) {
        this.location_pickup = location_pickup;
    }

    public String getLocation_availability() {
        return location_availability;
    }

    public void setLocation_availability(String location_availability) {
        this.location_availability = location_availability;
    }

    public String getLocation_logo() {
        return location_logo;
    }

    public void setLocation_logo(String location_logo) {
        this.location_logo = location_logo;
    }

    public String getLocation_ditance() {
        return location_ditance;
    }

    public void setLocation_ditance(String location_ditance) {
        this.location_ditance = location_ditance;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getLocation_postal() {
        return location_postal;
    }

    public void setLocation_postal(String location_postal) {
        this.location_postal = location_postal;
    }

    public String getLocation_image() {
        return location_image;
    }

    public void setLocation_image(String location_image) {
        this.location_image = location_image;
    }

    public String getLocation_price() {
        return location_price;
    }

    public void setLocation_price(String location_price) {
        this.location_price = location_price;
    }
}
