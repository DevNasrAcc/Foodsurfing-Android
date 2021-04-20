package com.adeel.foodsurfing.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.adeel.foodsurfing.AllLocations;
import com.adeel.foodsurfing.CustomTextView;
import com.adeel.foodsurfing.Publicvars;
import com.adeel.foodsurfing.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.adeel.foodsurfing.Adapters.BookingsAdapter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muhammad Adeel on 9/3/2017.
 */

public class Bookings extends Fragment{

    CustomTextView mid;
    private HashMap<String, List<String>> Hlisttotal_price,Hlistid,Hlistpickup_time,Hlistrestaurant_name,Hlistrestaurant_address,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time;
    private List<String> listtotal_price,listid,listCustomerName,listpickup_time,listrestaurant_name,listmeal_id,listmeal_title,listrestaurant_address,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,list_currency;
    private ExpandableListView BookingsExpandableList;
    public Boolean loggedIn = false;
    public String userid="0";
    private BookingsAdapter bookingsAdapter;

    public Bookings(){
    }
    public static Bookings newInstance() {
        Bookings fragment = new Bookings();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bookings, container,false);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ((AllLocations)getContext()).showprogress("Bestellungen werden gesucht...");
                            mid = (CustomTextView) view.findViewById(R.id.middle);
                            BookingsExpandableList = (ExpandableListView) view.findViewById(R.id.bookings_list);
                            BookingsExpandableList.setScrollingCacheEnabled(false);
                            BookingsExpandableList.setFastScrollEnabled(true);

                            GetBookingsList();

                            BookingsExpandableList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                                @Override
                                public void onGroupCollapse(int i) {

                                }
                            });

                            BookingsExpandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                @Override
                                public void onGroupExpand(int i) {
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 100000);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetBookingsList();
//            }
//        }, 1000);

        return view;
    }

    private void GetBookingsList() {
        listid = new ArrayList<String>();
        listCustomerName = new ArrayList<String>();
        listtotal_price = new ArrayList<String>();
        listpickup_time = new ArrayList<String>();
        list_currency = new ArrayList<String>();

        listrestaurant_name = new ArrayList<String>();
        listmeal_id = new ArrayList<String>();
        listmeal_title = new ArrayList<String>();
        listquantity = new ArrayList<String>();
        listprice = new ArrayList<String>();
        listrestaurant_opening_time= new ArrayList<String>();
        listrestaurant_closing_time= new ArrayList<String>();
        listrestaurant_address= new ArrayList<String>();

        Hlistid = new HashMap<String, List<String>>();
        Hlisttotal_price = new HashMap<String, List<String>>();
        Hlistpickup_time = new HashMap<String, List<String>>();
        Hlistrestaurant_name = new HashMap<String, List<String>>();
        Hlistmeal_id = new HashMap<String, List<String>>();
        Hlistmeal_title = new HashMap<String, List<String>>();
        Hlistquantity = new HashMap<String, List<String>>();
        Hlistprice = new HashMap<String, List<String>>();
        Hlistrestaurant_opening_time = new HashMap<String, List<String>>();
        Hlistrestaurant_closing_time = new HashMap<String, List<String>>();
        Hlistrestaurant_address = new HashMap<String, List<String>>();

        if(isOnline())
        {
            loggedIn = getbooleancache(Publicvars.UserSession,Publicvars.SessionState);
            if(loggedIn)

            {
                userid = getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID);
            }
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals3+"getTodayBookings",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    if (users.equals("OK"))
                                    {
                                        if(jsonObject.getJSONArray("order").length()>0)
                                        {
                                            JSONArray orders = jsonObject.getJSONArray("order");
                                            for (int a=0;a<orders.length();a++)
                                            {
                                                List<String> temporary = new ArrayList<String>();
                                                List<String> temporary2 = new ArrayList<String>();
                                                List<String> temporary3 = new ArrayList<String>();
                                                List<String> temporary4 = new ArrayList<String>();
                                                List<String> temporary5 = new ArrayList<String>();
                                                List<String> temporary6 = new ArrayList<String>();
                                                List<String> temporary7 = new ArrayList<String>();
                                                JSONObject jsonObjecto = orders.getJSONObject(a);
                                                String total_price = jsonObjecto.getString("total_price");
                                                String id = jsonObjecto.getString("id");
                                                String pickup_time = jsonObjecto.getString("pickup_time");
                                                String customer_name = jsonObjecto.getString("customer_name");
                                                String meal_currency = jsonObjecto.getString("currency");
                                                listtotal_price.add(total_price);
                                                listid.add(id);
                                                listpickup_time.add(pickup_time);
                                                listCustomerName.add(customer_name);

                                                if(jsonObjecto.getJSONArray("meals").length()>0)
                                                {
                                                    JSONArray meals = jsonObjecto.getJSONArray("meals");
                                                    for (int b=0;b<meals.length();b++)
                                                    {
                                                        JSONObject jsonObjectm = meals.getJSONObject(b);
                                                        String restaurant_name = jsonObjectm.getString("restaurant_name");
                                                       // String meal_id = jsonObjectm.getString("meal_id");
                                                        //String meal_currency = jsonObjectm.getString("currency");
                                                        String meal_title = jsonObjectm.getString("meal_title");
                                                        String quantity = jsonObjectm.getString("quantity");
                                                        String price = jsonObjectm.getString("price");
                                                        String restaurant_opening_time = jsonObjectm.getString("restaurant_opening_time");
                                                        String restaurant_closing_time = jsonObjectm.getString("restaurant_closing_time");
                                                        String restaurant_address = jsonObjectm.getString("restaurant_address");

                                                        list_currency.add(meal_currency);
                                                        listrestaurant_name.add(restaurant_name);
                                                        listmeal_id.add("");
                                                        listmeal_title.add(meal_title);
                                                        listquantity.add(quantity);
                                                        listprice.add(price);
                                                        listrestaurant_opening_time.add(restaurant_opening_time);
                                                        listrestaurant_closing_time.add(restaurant_closing_time);
                                                        listrestaurant_address.add(restaurant_address);

                                                        temporary.add("");
                                                        temporary2.add(quantity);
                                                        temporary3.add(meal_title);
                                                        temporary4.add(price);
                                                        temporary5.add(restaurant_closing_time);
                                                        temporary6.add(restaurant_opening_time);
                                                        temporary7.add(restaurant_address);
                                                    }
                                                    Hlistmeal_id.put(listid.get(a),temporary);
                                                    Hlistquantity.put(listid.get(a),temporary2);
                                                    Hlistmeal_title.put(listid.get(a),temporary3);
                                                    Hlistprice.put(listid.get(a),temporary4);
                                                    Hlistrestaurant_closing_time.put(listid.get(a),temporary5);
                                                    Hlistrestaurant_opening_time.put(listid.get(a),temporary6);
                                                    Hlistrestaurant_address.put(listid.get(a),temporary7);
                                                    Log.i("adeel",Hlistquantity.toString());
                                                    bookingsAdapter =  new BookingsAdapter(getContext(),listtotal_price,listCustomerName,listid,listpickup_time,listrestaurant_name,listmeal_id,listmeal_title,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time,list_currency,listrestaurant_address,Hlistrestaurant_address);
                                                    BookingsExpandableList.setAdapter(bookingsAdapter);
                                                    ((AllLocations)getContext()).hideprogress("");
                                                }
                                                else
                                                {
                                                    ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                                    mid.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                            mid.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                        mid.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                mid.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((AllLocations)getContext()).hideprogress("No Connection with server");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("user_id", userid);
                    //returning parameters
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
        else
        {
            ((AllLocations)getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

}
