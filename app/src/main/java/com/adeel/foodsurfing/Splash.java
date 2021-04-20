/**
 * <Splash> Package of the application which was set earlier.
 *
 * @author      : Muhammad Adeel
 * @owner       : Muhammad Adeel
 * @copyright   : Copyright (c) 2017 FoodSurfing
 * @created     : 22-06-2017
 * @modified    : 29-06-2017
 */
package com.adeel.foodsurfing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.Random;

public class Splash extends AppCompatActivity {

    RelativeLayout thelayout;
    ImageView mainlogo;

    /**
     * Splash screen view through setContentView in method OnCreate.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_splash);
        thelayout = (RelativeLayout) findViewById(R.id.thelayout);
        mainlogo = (ImageView) findViewById(R.id.mainlogo);
        thelayout.setBackgroundColor(Color.parseColor("#ffffff"));

        mainlogo.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.grow));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!getbooleancache("firsttime","itis"))
                {
                    startActivity(new Intent(Splash.this,Login_SignUp.class));
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    setbooleancache("firsttime","itis",true);
                }
                else
                {
                    startActivity(new Intent(Splash.this,AllLocations.class));
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                }
            }
        },3000);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public void setstringcache(String sharedpreferencename,String sharedpreferenceitemtext,String sharedpreferenceitemdata)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext,sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename,String sharedpreferenceitemtext)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename,Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext,"clear");
    }

    public void setintcache(String sharedpreferencename,String sharedpreferenceitemtext,int sharedpreferenceitemdata)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext,sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename,String sharedpreferenceitemtext)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext,0);
    }

    public void setbooleancache(String sharedpreferencename,String sharedpreferenceitemtext,Boolean sharedpreferenceitemdata)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext,sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename,String sharedpreferenceitemtext)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext,false);
    }
}
