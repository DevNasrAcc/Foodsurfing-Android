package com.adeel.foodsurfing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

class Locationitemadapter extends RecyclerView.Adapter<Locationitemadapter.MyViewHolder>  {

    Activity context;
    private ImageLoader mImageLoader;
    private List<Locationflower> locationlist;
    public static int a = 0;

    View circleshape;
    GradientDrawable shapecolor;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemdetailsheader,itemdetailspickuptext,itemdetailslocatetext;
        RelativeLayout itemdetailslayout;
        ImageView itemcover,itemdetailsicon;

        MyViewHolder(View view) {
            super(view);
            Typeface bold = Typeface.createFromAsset(view.getContext().getAssets(),"fonts/AmaticSC-Bold.ttf");
            Typeface regular = Typeface.createFromAsset(view.getContext().getAssets(),"fonts/AmaticSC-Regular.ttf");

            itemdetailsheader = (TextView) view.findViewById(R.id.itemdetailsheader);
            itemdetailsheader.setTypeface(bold);
            itemdetailspickuptext = (TextView) view.findViewById(R.id.itemdetailspickuptext);
            itemdetailslocatetext = (TextView) view.findViewById(R.id.itemdetailslocatetext);
            itemcover = (ImageView) view.findViewById(R.id.itemcover);
            itemdetailsicon = (ImageView) view.findViewById(R.id.itemdetailsicon);
            itemdetailsicon = (ImageView) view.findViewById(R.id.itemdetailsicon);

            circleshape = (View) view.findViewById(R.id.item_circle_shape);
            shapecolor = (GradientDrawable) circleshape.getBackground().getCurrent();
        }
    }

    Locationitemadapter(List<Locationflower> locationlist, Activity context) {
        this.locationlist = locationlist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_locations, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Locationflower movie = locationlist.get(position);

        holder.itemdetailsheader.setText(movie.getLocation_name());
        Glide.with(context).load(movie.getLocation_image())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.itemcover);

        Glide.with(context).load(movie.getLocation_logo())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.itemdetailsicon);

        holder.itemdetailslocatetext.setText(movie.getLocation_ditance() + " k.m");
        holder.itemdetailspickuptext.setText("Abholzeit: " + movie.getLocation_pickup());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof EventListener){
                    setstringcache("temporary","temporaryresid",movie.getLocation_id());
                    ((AllLocations)context).setheader(movie.getLocation_name());
                    ((EventListener)context).Locationpassed("restaurantdetails",movie.getLocation_name(),movie.getLocation_image(),movie.getLocation_logo(),"Adresse: " + movie.getLocation_address() + ", " + movie.getLocation_postal() + ", Duisburg Deutschland", movie.getLocation_availability());
                }
            }
        });

        if (movie.getLocation_availability().equals("true")) {
            shapecolor.setColor(Color.parseColor("#d71a6a"));
        }
        else {
            shapecolor.setColor(Color.parseColor("#2c2c2c"));
        }

    }

    private Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    public void statusbasic(View view, String header)
    {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(1000);
        snackbar.show();
    }

    @Override
    public int getItemCount() {
        return locationlist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }
}
