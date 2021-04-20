package com.adeel.foodsurfing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

class Mealitemadapter extends RecyclerView.Adapter<Mealitemadapter.MyViewHolder>  {

    private List<Productflower> productlist;
    private static int allcounter=0;
    private static int[] likephotosetter;
    public Boolean loggedIn = false;
    static Boolean itispressed=true;
    Timer t;
    int size=0;
    View viewer;

    Activity context;
    private List<Productflower> locationlist;
    public static int a = 0;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView pcover,plike;
        CustomTextView pname,pprice,pdescription,pres;

        MyViewHolder(View view) {
            super(view);
            //imagevw
            pcover = (ImageView) view.findViewById(R.id.pcover);
            plike = (ImageView) view.findViewById(R.id.plike);
            //text
            pname = (CustomTextView) view.findViewById(R.id.pname);
            pprice = (CustomTextView) view.findViewById(R.id.pprice);
            pdescription = (CustomTextView) view.findViewById(R.id.pdescription);
            pres = (CustomTextView) view.findViewById(R.id.pres);
        }
    }

    Mealitemadapter(List<Productflower> locationlist, Activity context) {
        this.locationlist = locationlist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_meals, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Productflower movie = locationlist.get(position);
        size = size + ProductsTabFragment.sizer;
        viewer = holder.itemView;
        Glide.with(context).load(movie.getProduct_image())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pcover);
        holder.pname.setText(movie.getProduct_title());
        holder.pprice.setText(movie.getProduct_price());
        holder.pdescription.setText(movie.getProduct_description());

        likephotosetter = new int[size];

        final CartHandler cartHandler = new CartHandler(context);

        holder.plike.setImageResource(R.drawable.iconunfavproduct);
        likephotosetter[position] = 0;
        context.getSharedPreferences("likephotosession", Context.MODE_PRIVATE).edit().putInt("likephoto"+position,0).apply();
        if(Integer.valueOf("0")>0)//////////////////////////////////////////////////////////////////////////////////////
        {
            holder.plike.setImageResource(R.drawable.iconfavproduct);
            likephotosetter[position] = 1;
            context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).edit().putInt("likephoto"+position,1).apply();
        }

        holder.plike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likephotosetter[position] = context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).getInt("likephoto"+position,0);
                loggedIn = context.getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getBoolean(Publicvars.SessionState, false);
                if (loggedIn)
                {
                    if (likephotosetter[position] == 0) {
                        //sendalike(movie.getProduct_id(), "active");
                        holder.plike.setImageResource(R.drawable.iconunfavproduct);
                        context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).edit().putInt("likephoto"+position,1).apply();
                    } else {
                        //sendalike(movie.getProduct_id(), "remove");
                        holder.plike.setImageResource(R.drawable.iconfavproduct);
                        context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).edit().putInt("likephoto"+position,0).apply();
                    }
                }
                else
                {
                    statusbasic("Login, um diese Funktion zu nutzen",holder.itemView);
                }
            }
        });

    }

    private Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    public void statusbasic(String header,View view)
    {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(1000);
        snackbar.show();
    }

    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

    private void sendalike(String meal_id,String user_id,String status) {
        if(isOnline())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals1+"get",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
                                    if (users.equals("OK")) {
                                        for (int a=0;a<jsonArray.length();a++)
                                        {
                                        }
                                    }
                                    else
                                    {
                                        statusbasic("Bitte überprüfen Sie Ihre Internetverbindung",viewer);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            statusbasic("No Connection with server",viewer);
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }
        else
        {
            statusbasic("Bitte überprüfen Sie Ihre Internetverbindung",viewer);
        }
    }
}
