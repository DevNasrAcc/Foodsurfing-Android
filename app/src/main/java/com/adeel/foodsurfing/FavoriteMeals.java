package com.adeel.foodsurfing;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FavoriteMeals extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private List<Productflower> detailer = new ArrayList<>();
    private RecyclerView mygrid;
    private Favoritemealsadapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    CustomTextView mid;
    public Boolean loggedIn = false;
    public String userid="0";
    // TODO: Rename and change types of parameters

    View view;
    public static int sizer=0;

    public FavoriteMeals() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavoriteMeals newInstance() {
        FavoriteMeals fragment = new FavoriteMeals();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_favorite_meals, container, false);

        ((AllLocations)getContext()).showprogress("Lieblingsessen werden gesucht...");
        mygrid = (RecyclerView) view.findViewById(R.id.allmeals);
        mAdapter = new Favoritemealsadapter(detailer, getActivity());
        mygrid.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mygrid.setLayoutManager(mLayoutManager);
        mygrid.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mygrid.setItemAnimator(new DefaultItemAnimator());
        mygrid.setAdapter(mAdapter);

        mid = (CustomTextView) view.findViewById(R.id.mid);
        Getrestaurants();

        return view;
    }

    private void Getrestaurants() {
        if(isOnline())
        {
            loggedIn = getbooleancache(Publicvars.UserSession,Publicvars.SessionState);
            if(loggedIn)
            {
                userid = getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID);
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals1+"getFavouriteMeals",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    if (users.equals("OK")) {
                                        if(jsonObject.getJSONArray("favourite_meals").length()>0)
                                        {
                                            JSONArray jsonArray = jsonObject.getJSONArray("favourite_meals");
                                            sizer = jsonArray.length();
                                            for (int a = 0; a < jsonArray.length(); a++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                                                Productflower fillit = new Productflower(getActivity(), jsonObject1.getString("meal_id"), jsonObject1.getString("title"), jsonObject1.getString("description"), jsonObject1.getString("quantity"), jsonObject1.getString("price"), jsonObject1.getString("restaurant_name"), jsonObject1.getString("status"), jsonObject1.getString("meal_image"), "true",jsonObject1.getString("currency"),jsonObject1.getString("pickup_time"), "", jsonObject1.getString("restaurant_id"));
                                                detailer.add(fillit);
                                            }
                                            mAdapter.notifyDataSetChanged();
                                            ((AllLocations) getContext()).hideprogress("");
                                        }
                                        else
                                        {
                                            ((AllLocations) getContext()).hideprogress("Keine Mahlzeiten gefunden");
                                            mid.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        sizer = 200;
                                        ((AllLocations)getContext()).hideprogress("Keine Mahlzeiten gefunden");
                                        mid.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Keine Mahlzeiten gefunden");
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
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("user_id", userid);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void changeitems(int position) {
        mygrid = (RecyclerView) view.findViewById(R.id.allmeals);
        mAdapter = new Favoritemealsadapter(detailer, getActivity());
        mygrid.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mygrid.setLayoutManager(mLayoutManager);
        mygrid.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mygrid.setItemAnimator(new DefaultItemAnimator());
        mygrid.setAdapter(mAdapter);

        mid = (CustomTextView) view.findViewById(R.id.mid);

        detailer.remove(position);
        mAdapter.notifyDataSetChanged();

        if(detailer.isEmpty())
        {
            mid.setVisibility(View.VISIBLE);
        }
    }
}
