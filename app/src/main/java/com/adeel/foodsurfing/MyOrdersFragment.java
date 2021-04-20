package com.adeel.foodsurfing;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

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

import static android.content.Context.MODE_PRIVATE;

public class MyOrdersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Boolean loggedIn = false;
    public String userid="0";
    View view;
    CustomTextView mid;

    private ExpandableListView expandableListView;
    private MyOrdersAdapter expandableListAdapter;
    private List<String> listtotal_price,listid,listpickup_time,listrestaurant_name,listmeal_id,listmeal_title,listrestaurant_address,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,list_currency;
    private HashMap<String, List<String>> Hlisttotal_price,Hlistid,Hlistpickup_time,Hlistrestaurant_name,Hlistrestaurant_address,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ((AllLocations)getContext()).showprogress("Bestellungen werden gesucht...");
        mid = (CustomTextView) view.findViewById(R.id.middle);
        expandableListView = (ExpandableListView) view.findViewById(R.id.recycler_view);
        expandableListView.setScrollingCacheEnabled(false);
        expandableListView.setFastScrollEnabled(true);
        GetOrders();

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
            }
        });

        //Getorders1();
        return view;
    }

    private void GetOrders() {
        listid = new ArrayList<String>();
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
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals3+"getOrders",
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
                                                listtotal_price.add(total_price);
                                                listid.add(id);
                                                listpickup_time.add(pickup_time);

                                                if(jsonObjecto.getJSONArray("meals").length()>0)
                                                {
                                                    JSONArray meals = jsonObjecto.getJSONArray("meals");
                                                    for (int b=0;b<meals.length();b++)
                                                    {
                                                        JSONObject jsonObjectm = meals.getJSONObject(b);
                                                        String restaurant_name = jsonObjectm.getString("restaurant_name");
                                                        String meal_id = jsonObjectm.getString("meal_id");
                                                        String meal_currency = jsonObjectm.getString("currency");
                                                        String meal_title = jsonObjectm.getString("meal_title");
                                                        String quantity = jsonObjectm.getString("quantity");
                                                        String price = jsonObjectm.getString("price");
                                                        String restaurant_opening_time = jsonObjectm.getString("restaurant_opening_time");
                                                        String restaurant_closing_time = jsonObjectm.getString("restaurant_closing_time");
                                                        String restaurant_address = jsonObjectm.getString("restaurant_address");

                                                        list_currency.add(meal_currency);
                                                        listrestaurant_name.add(restaurant_name);
                                                        listmeal_id.add(meal_id);
                                                        listmeal_title.add(meal_title);
                                                        listquantity.add(quantity);
                                                        listprice.add(price);
                                                        listrestaurant_opening_time.add(restaurant_opening_time);
                                                        listrestaurant_closing_time.add(restaurant_closing_time);
                                                        listrestaurant_address.add(restaurant_address);

                                                        temporary.add(meal_id);
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
                                                    expandableListAdapter =  new MyOrdersAdapter(getContext(),listtotal_price,listid,listpickup_time,listrestaurant_name,listmeal_id,listmeal_title,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time,list_currency,listrestaurant_address,Hlistrestaurant_address);
                                                    expandableListView.setAdapter(expandableListAdapter);
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

    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
