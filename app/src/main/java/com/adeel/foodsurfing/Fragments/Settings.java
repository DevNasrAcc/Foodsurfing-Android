package com.adeel.foodsurfing.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeel.foodsurfing.Adapters.SettingsAdapter;
import com.adeel.foodsurfing.AllLocations;
import com.adeel.foodsurfing.CustomTextView;
import com.adeel.foodsurfing.GetSetMethod.ListSettings;
import com.adeel.foodsurfing.GetSetMethod.SettingsChildResponse;
import com.adeel.foodsurfing.GetSetMethod.SettingsHeaderResponse;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muhammad Adeel on 9/7/2017.
 */

public class Settings extends Fragment {

    private List<SettingsHeaderResponse> headerList = new ArrayList<>();
    private List<SettingsChildResponse> ChildList = new ArrayList<>();
    private List<ListSettings> listSettings ;
    private RecyclerView settingRecycler;
    private SettingsAdapter settingsAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    public Boolean loggedIn = false;
    public String userid="0";
    CustomTextView midSettings;
    View view;
    public static int sizeSettingsResponse=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container,false);

        settingRecycler = (RecyclerView) view.findViewById(R.id.RecyclerSettings);

        settingRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        settingRecycler.setLayoutManager(mLayoutManager);
        settingRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        settingRecycler.setItemAnimator(new DefaultItemAnimator());
        settingRecycler.setAdapter(settingsAdapter);
        midSettings = (CustomTextView) view.findViewById(R.id.midSettings);

        GetrestaurantsSettings();
        return view;
    }

    private void GetrestaurantsSettings() {
        if(isOnline())
        {
            loggedIn = getbooleancache(Publicvars.UserSession,Publicvars.SessionState);
            if(loggedIn)
            {
                userid = getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID);
            }
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals1+"getRestaurantSettings",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                       listSettings = new ArrayList<>();

                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    if (users.equals("OK")) {
                                        ListSettings listSettingsNew = new ListSettings();

                                        JSONObject jsonArray = jsonObject.getJSONObject("restaurant");

                                        SettingsHeaderResponse headerResponse = new SettingsHeaderResponse(getActivity(), jsonArray.getString("id"),
                                                    jsonArray.getString("name"),jsonArray.getString("description"),
                                                    jsonArray.getString("currency"),jsonArray.getString("logo"),
                                                    jsonArray.getString("start_pickup_time"),jsonArray.getString("end_pickup_time"));
                                            headerList.add(headerResponse);
                                            listSettingsNew.setHeaderResponses(headerList);

                                            JSONArray rest_meals = jsonArray.getJSONArray("restaurant_meals");
                                        if (rest_meals.length()>0)
                                        {

                                            for (int b=0;b<rest_meals.length();b++)
                                            {
                                                sizeSettingsResponse = rest_meals.length();
                                                JSONObject jsonObjectChild = rest_meals.getJSONObject(b);
                                                SettingsChildResponse childResponse = new SettingsChildResponse(getActivity(),
                                                        jsonObjectChild.getString("id"),jsonObjectChild.getString("title"),
                                                        jsonObjectChild.getString("description"),jsonObjectChild.getString("quantity"),
                                                            jsonObjectChild.getString("price"), jsonObjectChild.getString("image"));
                                                ChildList.add(childResponse);


                                            }
                                            listSettingsNew.setChildResponses(ChildList);
                                            listSettings.add(listSettingsNew);


                                        }

                                        //settingsAdapter = new SettingsAdapter(headerList,ChildList, getActivity());
                                        settingsAdapter = new SettingsAdapter(listSettings, getActivity());
                                        settingRecycler.setAdapter(settingsAdapter);

                                        ((AllLocations)getContext()).hideprogress("");
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ((AllLocations)getContext()).hideprogress("");
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Keine Mahlzeiten gefunden");
                                midSettings.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((AllLocations)getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("restaurant_id", getstringcache("temporary","temporaryresid"));
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
