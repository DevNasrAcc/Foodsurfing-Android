package com.adeel.foodsurfing;

public class GetSetProducts {

    //private variables
    public int Productid,Productquantity,Productquantityu,Productrestaurantid;
    public float Productprice;
    public String is_favorite,Productcurrency;
    public String Producttitle,Productdescription,Productstatus,Productimage,Productpickup;
    // Empty constructor
    public GetSetProducts(){

    }
    // constructor
    public GetSetProducts(int Productid,int Productquantity,int Productquantityu,int Productrestaurantid,float Productprice,String Producttitle,String Productdescription,String Productstatus,String Productimage,String is_favorite,String Productcurrency, String Productpickup){
        this.Productid = Productid;
        this.Productquantity = Productquantity;
        this.Productprice = Productprice;
        this.Productquantityu = Productquantityu;
        this.Producttitle = Producttitle;
        this.Productdescription = Productdescription;
        this.Productstatus = Productstatus;
        this.Productimage = Productimage;
        this.Productpickup = Productpickup;
        this.Productcurrency = Productcurrency;
        this.Productrestaurantid = Productrestaurantid;
        this.is_favorite = is_favorite;
    }

    public String getProductpickup() {
        return Productpickup;
    }

    public void setProductpickup(String productpickup) {
        Productpickup = productpickup;
    }

    public String getProductcurrency() {
        return Productcurrency;
    }

    public void setProductcurrency(String productcurrency) {
        Productcurrency = productcurrency;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public int getProductquantityu() {
        return Productquantityu;
    }

    public void setProductquantityu(int productquantityu) {
        Productquantityu = productquantityu;
    }

    public int getProductid() {
        return Productid;
    }

    public void setProductid(int productid) {
        Productid = productid;
    }

    public int getProductquantity() {
        return Productquantity;
    }

    public void setProductquantity(int productquantity) {
        Productquantity = productquantity;
    }

    public int getProductrestaurantid() {
        return Productrestaurantid;
    }

    public void setProductrestaurantid(int productrestaurantid) {
        Productrestaurantid = productrestaurantid;
    }

    public float getProductprice() {
        return Productprice;
    }

    public void setProductprice(float productprice) {
        Productprice = productprice;
    }

    public String getProducttitle() {
        return Producttitle;
    }

    public void setProducttitle(String producttitle) {
        Producttitle = producttitle;
    }

    public String getProductdescription() {
        return Productdescription;
    }

    public void setProductdescription(String productdescription) {
        Productdescription = productdescription;
    }

    public String getProductstatus() {
        return Productstatus;
    }

    public void setProductstatus(String productstatus) {
        Productstatus = productstatus;
    }

    public String getProductimage() {
        return Productimage;
    }

    public void setProductimage(String productimage) {
        Productimage = productimage;
    }
}