package com.adeel.foodsurfing.GetSetMethod;

import android.app.Activity;

/**
 * Created by Muhammad Adeel on 9/7/2017.
 */

public class SettingsHeaderResponse {

    public String getRestaurant_id() {
        return Restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        Restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartPickupTime() {
        return startPickupTime;
    }

    public void setStartPickupTime(String name) {
        this.startPickupTime = startPickupTime;
    }

    public String getEndPickupTime() {
        return endPickupTime;
    }

    public void setEndPickupTime(String name) {
        this.endPickupTime = endPickupTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getHeaderlogo() {
        return Headerlogo;
    }

    public void setHeaderlogo(String headerlogo) {
        Headerlogo = headerlogo;
    }

    private String Restaurant_id;
    private String name;
    private String description;
    private String currency;
    private String Headerlogo;
    private String startPickupTime;
    private String endPickupTime;

    Activity context;

    public SettingsHeaderResponse(Activity context, String Restaurant_id, String name, String description, String currency, String Headerlogo, String startPickupTime, String endPickupTime){
        this.Restaurant_id = Restaurant_id;
        this.name = name;
        this.description = description;
        this.Headerlogo = Headerlogo;
        this.context = context;
        this.startPickupTime = startPickupTime;
        this.endPickupTime = endPickupTime;
    }
}
