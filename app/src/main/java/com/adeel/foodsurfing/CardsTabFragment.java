package com.adeel.foodsurfing;

import android.app.Activity;
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
import android.view.MotionEvent;
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

public class CardsTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Locationflower> detailer = new ArrayList<>();
    private RecyclerView mygrid;
    private Locationitemadapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    CustomTextView mid;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int temp = 0;

    View view;

    public CardsTabFragment() {
        // Required empty public constructor
    }

    public static CardsTabFragment newInstance(String param1, String param2) {
        CardsTabFragment fragment = new CardsTabFragment();
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
        view = inflater.inflate(R.layout.fragment_cards, container,false);

        mygrid = (RecyclerView) view.findViewById(R.id.alllocations);
        mAdapter = new Locationitemadapter(detailer, getActivity());
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals1+"getFilteredRestaurants",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
                                    if (users.equals("OK"))
                                    {
                                        if(jsonObject.getJSONArray("restaurants").length()>0)
                                        {
                                            for (int a=0;a<jsonArray.length();a++)
                                            {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(a);

                                                Geocoder gc = new Geocoder(getContext());
                                                if (gc.isPresent()) {
                                                    try {
                                                        List<Address> list = gc.getFromLocationName(jsonObject1.getString("address"), 1);
                                                        if (list.size() > 0)
                                                        {
                                                            Address address = list.get(0);
                                                            double lat = address.getLatitude();
                                                            double lng = address.getLongitude();

                                                            Double latitude = 0.0;
                                                            Double longitude = 0.0;

                                                            if(!getstringcache(Publicvars.My_Location,Publicvars.My_latitude).equals("clear"))
                                                            {
                                                                longitude = Double.valueOf(getstringcache(Publicvars.My_Location,Publicvars.My_longitude));
                                                                latitude = Double.valueOf(getstringcache(Publicvars.My_Location,Publicvars.My_latitude));
                                                            }

                                                            Location loc1 = new Location("");
                                                            loc1.setLatitude(lat);
                                                            loc1.setLongitude(lng);

                                                            Location loc2 = new Location("");
                                                            loc2.setLatitude(latitude);
                                                            loc2.setLongitude(longitude);

                                                            float distanceInMeters = loc1.distanceTo(loc2) / 1000;
                                                            double roundOff = (double) Math.round(distanceInMeters * 10) / 10;

                                                            if(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMin)<=roundOff&&getintcache(Publicvars.My_Location,Publicvars.My_DistanceMax)>roundOff)
                                                            {
                                                                if (a==0)
                                                                {
                                                                    ((AllLocations)getContext()).hideprogress("");
                                                                }
                                                                Locationflower fillit = new Locationflower(getActivity(), jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getString("address"),jsonObject1.getString("img"),"12",String.valueOf(roundOff),jsonObject1.getString("logo"),jsonObject1.getString("pickup"), jsonObject1.getString("postal"), jsonObject1.getString("availability"));
                                                                detailer.add(fillit);
                                                            }
                                                            else
                                                            {
                                                                temp++;
                                                                if(temp==jsonArray.length())
                                                                {
                                                                    ((AllLocations)getContext()).hideprogress("Keine Gastronomiebetriebe in Ihrer Nähe...");
                                                                    mid.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        }
                                                        else
                                                        {
                                                            ((AllLocations)getContext()).hideprogress("");
                                                            Locationflower fillit = new Locationflower(getActivity(), jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getString("address"),jsonObject1.getString("img"),"12","N/A",jsonObject1.getString("logo"),jsonObject1.getString("pickup"), jsonObject1.getString("postal"), jsonObject1.getString("availability"));
                                                            detailer.add(fillit);
                                                        }
                                                    } catch(IOException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        else
                                        {
                                            ((AllLocations)getContext()).hideprogress("Keine Gastronomiebetriebe in Ihrer Nähe...");
                                            mid.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Keine Gastronomiebetriebe in Ihrer Nähe...");
                                mid.setVisibility(View.VISIBLE);
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

                    String longitude = "24.918027";
                    String latitude = "67.097166";
                    String timemin = "0";
                    String timemax = "24";
                    String distance = "100";
                    if(!getstringcache(Publicvars.My_Location,Publicvars.My_latitude).equals("clear"))
                    {
                        longitude = getstringcache(Publicvars.My_Location,Publicvars.My_longitude);
                        latitude = getstringcache(Publicvars.My_Location,Publicvars.My_latitude);
                    }
                    if(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMax)>0)
                    {
                        distance = String.valueOf(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMax));
                    }
                    if(getintcache(Publicvars.My_Location,Publicvars.My_TimeMax)!=0)
                    {
                        timemin = String.valueOf(getintcache(Publicvars.My_Location,Publicvars.My_TimeMin));
                        timemax = String.valueOf(getintcache(Publicvars.My_Location,Publicvars.My_TimeMax));

                    }
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("longitude", longitude);
                    params.put("latitude", latitude);
                    params.put("distance", distance);
                    params.put("opening_time", timemin+":00");
                    params.put("closing_time", timemax+":00");
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

}
