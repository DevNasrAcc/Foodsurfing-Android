package com.adeel.foodsurfing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CartHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "foodsurfing";

    // Contacts table name
    private static final String TABLE_ADDTOCART = "product";

    // Contacts Table Columns names
    private static final String KEY_PRODUCTID = "productid";
    private static final String KEY_PRODUCTQUANTITY = "productquantity";
    private static final String KEY_RESTAURANTID = "restaurantid";
    private static final String KEY_PRODUCTQUANTITYU = "productquantityu";
    private static final String KEY_PRODUCTTITLE = "producttitle";
    private static final String KEY_PRODUCTDESCRIPTION = "productdescription";
    private static final String KEY_PRODUCTPRICE = "productprice";
    private static final String KEY_PRODUCTSTATUS = "productstatus";
    private static final String KEY_PRODUCTIMAGE = "productimage";
    private static final String KEY_IS_FAVORITE = "is_favorite";
    private static final String KEY_PRODUCTCURRENCY = "productcurrency";


    public CartHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_ADDTOCART + "("
                + KEY_PRODUCTID + " INTEGER," + KEY_PRODUCTQUANTITY + " INTEGER,"
                + KEY_PRODUCTQUANTITYU + " INTEGER," + KEY_PRODUCTTITLE + " TEXT,"
                + KEY_PRODUCTDESCRIPTION + " TEXT," + KEY_PRODUCTPRICE + " REAL,"
                + KEY_PRODUCTSTATUS + " TEXT," + KEY_PRODUCTIMAGE + " TEXT,"
                + KEY_IS_FAVORITE + " TEXT," + KEY_PRODUCTCURRENCY + " TEXT,"
                + KEY_RESTAURANTID + " INTEGER" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDTOCART);
        // Create tables again
        onCreate(db);
    }

    void addProduct(GetSetProducts location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCTID, location.getProductid());
        values.put(KEY_PRODUCTQUANTITY, location.getProductquantity());
        values.put(KEY_PRODUCTQUANTITYU, location.getProductquantityu());
        values.put(KEY_PRODUCTTITLE, location.getProducttitle());
        values.put(KEY_PRODUCTDESCRIPTION, location.getProductdescription());
        values.put(KEY_PRODUCTPRICE, location.getProductprice());
        values.put(KEY_PRODUCTIMAGE, location.getProductimage());
        values.put(KEY_RESTAURANTID, location.getProductrestaurantid());
        values.put(KEY_PRODUCTSTATUS, location.getProductstatus());
        values.put(KEY_IS_FAVORITE, location.getIs_favorite());
        values.put(KEY_PRODUCTCURRENCY, location.getProductcurrency());
        // Inserting Row
        db.insert(TABLE_ADDTOCART, null, values);
        db.close(); // Closing database connection
    }

    public int getProduct(int proid,int resid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADDTOCART, new String[]{KEY_PRODUCTID,
                        KEY_PRODUCTQUANTITY,KEY_PRODUCTQUANTITYU,KEY_PRODUCTTITLE,
                        KEY_PRODUCTDESCRIPTION,KEY_PRODUCTPRICE,KEY_PRODUCTSTATUS,
                        KEY_PRODUCTIMAGE,KEY_IS_FAVORITE ,KEY_RESTAURANTID,KEY_PRODUCTCURRENCY},
                KEY_PRODUCTID + " = ? AND " + KEY_RESTAURANTID + " = ?",
                new String[]{String.valueOf(proid), String.valueOf(resid)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
            return Integer.valueOf(cursor.getString(2));
    }

    public List<GetSetProducts> getAllProducts() {
        List<GetSetProducts> contactList = new ArrayList<GetSetProducts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ADDTOCART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GetSetProducts contact = new GetSetProducts();
                contact.setProductid(Integer.parseInt(cursor.getString(0)));
                contact.setProductquantity(Integer.parseInt(cursor.getString(1)));
                contact.setProductquantityu(Integer.parseInt(cursor.getString(2)));
                contact.setProducttitle(cursor.getString(3));
                contact.setProductdescription(cursor.getString(4));
                contact.setProductprice(Float.parseFloat(cursor.getString(5)));
                contact.setProductstatus(cursor.getString(6));
                contact.setProductimage(cursor.getString(7));
                contact.setIs_favorite(cursor.getString(8));
                contact.setProductrestaurantid(Integer.parseInt(cursor.getString(10)));
                contact.setProductcurrency(cursor.getString(9));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int updateProduct(GetSetProducts location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCTID, location.getProductid());
        values.put(KEY_PRODUCTQUANTITY, location.getProductquantity());
        values.put(KEY_PRODUCTQUANTITYU, location.getProductquantityu());
        values.put(KEY_PRODUCTTITLE, location.getProducttitle());
        values.put(KEY_PRODUCTDESCRIPTION, location.getProductdescription());
        values.put(KEY_PRODUCTPRICE, location.getProductprice());
        values.put(KEY_PRODUCTIMAGE, location.getProductimage());
        values.put(KEY_RESTAURANTID, location.getProductrestaurantid());
        values.put(KEY_PRODUCTSTATUS, location.getProductstatus());
        values.put(KEY_IS_FAVORITE, location.getIs_favorite());
        values.put(KEY_PRODUCTCURRENCY, location.getProductcurrency());

        //db.update()
        return db.update(TABLE_ADDTOCART, values, KEY_PRODUCTID + " = ? AND " + KEY_RESTAURANTID + " = ?",
                new String[] { String.valueOf(location.getProductid()),String.valueOf(location.getProductrestaurantid()) });
    }

    public void deleteProduct(int proid,int resid) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ADDTOCART, KEY_PRODUCTID + " = ? AND " + KEY_RESTAURANTID + " = ?",
                new String[] { String.valueOf(proid),String.valueOf(resid) });
        db.close();
    }

    public int deleteallProducts() {
        String countQuery = "DELETE FROM " + TABLE_ADDTOCART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int co = cursor.getCount();
        cursor.close();
        return co;
    }

    public int getProductCount(int proid,int resid) {
        String countQuery = "SELECT * FROM " + TABLE_ADDTOCART + " WHERE productid = '"+proid+"' AND restaurantid = '"+resid+"' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int co = cursor.getCount();
        cursor.close();
        return co;
    }

    public int getallProductsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ADDTOCART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int co = cursor.getCount();
        cursor.close();
        return co;
    }

    public int getallProductsQuantity() {
        String countQuery = "SELECT SUM(productquantity) FROM " + TABLE_ADDTOCART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getProductsQuantity(int proid,int resid) {
        String countQuery = "SELECT productquantity FROM " + TABLE_ADDTOCART + " WHERE productid = "+proid+" AND restaurantid = "+resid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getProductsQuantityU(int proid,int resid) {
        String countQuery = "SELECT productquantityu FROM " + TABLE_ADDTOCART + " WHERE productid = "+proid+" AND restaurantid = "+resid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public Float getProductPrice(int proid,int resid) {
        String countQuery = "SELECT productprice FROM " + TABLE_ADDTOCART + " WHERE productid = "+proid+" AND restaurantid = "+resid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getFloat(0);
    }

    public String getProductLike(String proid,String resid) {
        String countQuery = "SELECT is_favorite FROM " + TABLE_ADDTOCART + " WHERE productid = "+proid+" AND restaurantid = "+resid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getString(0);
    }

    public float getallProductsPrice() {
        String countQuery = "SELECT (productprice*productquantity) AS rec FROM " + TABLE_ADDTOCART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getFloat(0);
    }
}
