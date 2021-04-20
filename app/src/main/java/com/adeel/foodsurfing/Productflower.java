package com.adeel.foodsurfing;

import android.app.Activity;


/**
 * Created on 15/01/16.
 */
public class Productflower {
    private String product_id,product_title,product_description,product_quantity,product_price,product_restaurant_id,product_status,product_image, product_pickup, product_availability, product_restaurant;
    private Activity context;
    String is_favorite,product_currency;

    public Productflower() {
    }
    public Productflower(Activity context, String product_id,String product_title,String product_description,
                         String product_quantity,String product_price,String product_restaurant_id,
                         String product_status,String product_image,String is_favorite,String product_currency,String product_pickup, String product_availability, String product_restaurant)
    {
        this.context = context;
        this.product_id = product_id;
        this.product_title = product_title;
        this.product_description = product_description;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.product_restaurant_id = product_restaurant_id;
        this.product_status = product_status;
        this.product_image = product_image;
        this.is_favorite = is_favorite;
        this.product_currency = product_currency;
        this.product_pickup = product_pickup;
        this.product_availability = product_availability;
        this.product_restaurant = product_restaurant;
    }

    public String getProduct_currency() {
        return product_currency;
    }

    public void setProduct_currency(String product_currency) {
        this.product_currency = product_currency;
    }

    public String getProduct_availability() {
        return product_availability;
    }

    public String getProduct_restaurant() {
        return product_restaurant;
    }

    public void setProduct_restaurant(String product_restaurant) {
        this.product_restaurant = product_restaurant;
    }

    public void setProduct_availability(String product_availability) {
        this.product_availability = product_availability;
    }

    public String getProduct_pickup() {
        return product_pickup;
    }

    public void setProduct_pickup(String product_pickup) {
        this.product_pickup = product_pickup;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_restaurant_id() {
        return product_restaurant_id;
    }

    public void setProduct_restaurant_id(String product_restaurant_id) {
        this.product_restaurant_id = product_restaurant_id;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }
}
