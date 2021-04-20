package com.adeel.foodsurfing;

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

public class AboutTabFragment extends Fragment {

    CustomTextView mid;
    int temp = 0;
    View view;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public Boolean loggedIn = false;
    public String userid="0";

    public AboutTabFragment() {
        // Required empty public constructor
    }

    /**
     * @param param1
     * @return
     */
    public static AboutTabFragment newInstance(String param1) {
        AboutTabFragment fragment = new AboutTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container,false);

        mid = (CustomTextView) view.findViewById(R.id.mid);

        Getrestaurants();

        return view;
    }

    private void Getrestaurants() {
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
                                    if (users.equals("OK"))
                                    {
                                        if(jsonObject.getJSONArray("restaurants").length()>0)
                                        {
                                            for (int a=0;a<jsonArray.length();a++)
                                            {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                                                if(jsonObject1.getString("id").equals(getstringcache("temporary","temporaryresid")))
                                                {
                                                    mid.setText(jsonObject1.getString("description"));
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            statusbasic("No Connection with server",view);
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }
        else
        {
            statusbasic("Bitte überprüfen Sie Ihre Internetverbindung",view);
        }
    }

    /**
     * @param header
     * @param view
     */
    public void statusbasic(String header,View view)
    {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(10000);
        snackbar.show();
    }

    /**
     * @param sharedpreferencename
     * @param sharedpreferenceitemtext
     * @param sharedpreferenceitemdata
     */
    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    /**
     * @param sharedpreferencename
     * @param sharedpreferenceitemtext
     * @return
     */
    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    /**
     * @param sharedpreferencename
     * @param sharedpreferenceitemtext
     * @param sharedpreferenceitemdata
     */
    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    /**
     * @param sharedpreferencename
     * @param sharedpreferenceitemtext
     * @return
     */
    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    /**
     * @param sharedpreferencename
     * @param sharedpreferenceitemtext
     * @param sharedpreferenceitemdata
     */
    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    /**
     * @param sharedpreferencename
     * @param sharedpreferenceitemtext
     * @return
     */
    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    /**
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}