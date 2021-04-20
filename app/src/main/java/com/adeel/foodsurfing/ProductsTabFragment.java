package com.adeel.foodsurfing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProductsTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private List<Productflower> detailer = new ArrayList<>();
    private RecyclerView mygrid;
    private Productitemadapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    CustomTextView mid;
    // TODO: Rename and change types of parameters
    private String mParam1;
    int temp = 0;

    public static int sizer=0;
    public Boolean loggedIn = false;
    public String userid="0";

    View view;

    public ProductsTabFragment() {
        // Required empty public constructor
    }

    public static ProductsTabFragment newInstance(String param1) {
        ProductsTabFragment fragment = new ProductsTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_products, container,false);

        mygrid = (RecyclerView) view.findViewById(R.id.alllocations);
        mAdapter = new Productitemadapter(detailer, getActivity());
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
        try {
            if (isOnline()) {
                loggedIn = getbooleancache(Publicvars.UserSession, Publicvars.SessionState);
                if (loggedIn) {
                    userid = getstringcache(Publicvars.UserSession, Publicvars.KEY_USERID);
                }
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, Publicvars.Globals1 + "getRestaurantMeals",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.length() > 0) {
                                    JSONObject jsonObject;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String users = jsonObject.getString("error");
                                        if (users.equals("OK")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("restaurant_meals");
                                            sizer = jsonArray.length();
                                            for (int a = 0; a < jsonArray.length(); a++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(a);

                                                Productflower fillit = new Productflower(getActivity(), jsonObject1.getString("id"), jsonObject1.getString("title"), jsonObject1.getString("description"), jsonObject1.getString("quantity"), jsonObject1.getString("price"), jsonObject1.getString("restaurant_id"), jsonObject1.getString("status"), jsonObject1.getString("img"), jsonObject1.getString("is_favorite"), jsonObject1.getString("currency"), jsonObject1.getString("pickup_time"), jsonObject1.getString("availability"), "");
                                                detailer.add(fillit);
                                            }
                                            mAdapter.notifyDataSetChanged();
                                            ((AllLocations) getContext()).hideprogress("");
                                        } else {
                                            ((AllLocations) getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ((AllLocations) getContext()).hideprogress("");
                                } else {
                                    ((AllLocations) getContext()).hideprogress("Keine Mahlzeiten gefunden");
                                    mid.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ((AllLocations) getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("restaurant_id", getstringcache("temporary", "temporaryresid"));
                        params.put("user_id", userid);
                        //returning parameters
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            } else {
                ((AllLocations) getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
