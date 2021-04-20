package com.adeel.foodsurfing.GetSetMethod;

import android.app.Activity;

/**
 * Created by Muhammad Adeel on 9/7/2017.
 */

public class SettingsChildResponse {


    private String restaurant_meals_Id;
    private String title;
    private String restaurant_meals_description;
    private String quantity;
    private String price;


    public SettingsChildResponse(Activity context, String restaurant_meals_Id, String title, String restaurant_meals_description,
                                 String quantity, String price, String meals_image)
    {
        this.context = context;

        this.restaurant_meals_Id = restaurant_meals_Id;
        this.title = title;
        this.restaurant_meals_description = restaurant_meals_description;
        this.quantity = quantity;
        this.price = price;
        this.meals_image = meals_image;

    }




    public String getRestaurant_meals_Id() {
        return restaurant_meals_Id;
    }

    public void setRestaurant_meals_Id(String restaurant_meals_Id) {
        this.restaurant_meals_Id = restaurant_meals_Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRestaurant_meals_description() {
        return restaurant_meals_description;
    }

    public void setRestaurant_meals_description(String restaurant_meals_description) {
        this.restaurant_meals_description = restaurant_meals_description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMeals_image() {
        return meals_image;
    }

    public void setMeals_image(String meals_image) {
        this.meals_image = meals_image;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    private String meals_image;

    private Activity context;




}
