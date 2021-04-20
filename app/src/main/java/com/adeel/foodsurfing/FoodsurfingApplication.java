package com.adeel.foodsurfing;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
//import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDexApplication;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class FoodsurfingApplication extends MultiDexApplication {

    Context context;
    public String MyError="This is an Error";

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(getApplicationContext());
    }

    @Override
    public void onCreate() {
        context = FoodsurfingApplication.this;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error Crash " + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
                //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        });
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        Log.d("","Memory Low...");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Log.d("","Crashed...");
        super.onTerminate();
    }
}
