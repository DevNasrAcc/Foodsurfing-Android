package com.adeel.foodsurfing.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.adeel.foodsurfing.AllLocations;
import com.adeel.foodsurfing.CustomEditText;
import com.adeel.foodsurfing.CustomTextView;
import com.adeel.foodsurfing.GetSetMethod.ListSettings;
import com.adeel.foodsurfing.GetSetMethod.SettingsHeaderResponse;
import com.adeel.foodsurfing.GetSetMethod.SettingsChildResponse;
import com.adeel.foodsurfing.Publicvars;
import com.adeel.foodsurfing.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad Adeel on 9/7/2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    private List<ListSettings> SettingsHeaderList;
    public Boolean loggedIn = false;
    View viewer;
    Activity context;

    @Override
    public SettingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_header, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SettingsAdapter.MyViewHolder holder, final int position) {
        final ListSettings Response = SettingsHeaderList.get(position);
        final List<SettingsHeaderResponse> HeaderSettingsResponse = Response.getHeaderResponses();

        SubSettingsLayoutAdapter subSettingsLayoutAdapterAdapter =
                new SubSettingsLayoutAdapter(Response.getChildResponses(), context);
        holder.childRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager  = new LinearLayoutManager(context);
        holder.childRecycler.setLayoutManager(mLayoutManager);
        holder.childRecycler.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        holder.childRecycler.setItemAnimator(new DefaultItemAnimator());
        holder.childRecycler.setAdapter(subSettingsLayoutAdapterAdapter);

                viewer = holder.itemView;

        Glide.with(context).load(HeaderSettingsResponse.get(0).getHeaderlogo())
                .thumbnail(0.5f)
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.HeaderLogo);
        holder.HeaderRestTitle.setText(HeaderSettingsResponse.get(0).getName());
        holder.HeaderDescription.setText(HeaderSettingsResponse.get(0).getDescription());
        holder.settingsStartTime.setText(HeaderSettingsResponse.get(0).getStartPickupTime());
        holder.settingsEndTime.setText(HeaderSettingsResponse.get(0).getEndPickupTime());

        holder.headerBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pickupTime = holder.headerSpinner.getSelectedItem().toString();
                String startPickupTime = holder.settingsStartTime.getText().toString();
                String endPickupTime = holder.settingsEndTime.getText().toString();
                updateRestaurantPickupTime(HeaderSettingsResponse.get(0).getRestaurant_id(), pickupTime, startPickupTime, endPickupTime);
            }
        });
    }
    private void updateRestaurantPickupTime(final String restaurant_id, final String pickup_time, final String start_pickup_time, final String end_pickup_time) {
        if(isOnline())
        {

            ((AllLocations)context).showprogress("Bestellungen werden gesucht...");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Publicvars.Globals1+"updateRestaurantPickupTime",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((AllLocations)context).hideprogress("");
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String responseOk = jsonObject.getString("error");
                                    if (responseOk.equals("OK")){

                                        Toast.makeText(context, "Abholzeit aktualisiert", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(context, "Time not set", Toast.LENGTH_SHORT).show();

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
                            ((AllLocations)context).hideprogress("");
                            Toast.makeText(context, "Bitte überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("restaurant_id", restaurant_id);
                    params.put("pickup_time", pickup_time);
                    params.put("start_pickup_time", start_pickup_time);
                    params.put("end_pickup_time", end_pickup_time);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }
        else
        {
            Toast.makeText(context, "Bitte überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return SettingsHeaderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView HeaderLogo;
        RecyclerView childRecycler;
        CustomTextView HeaderRestTitle,HeaderDescription;
        CustomEditText settingsStartTime, settingsEndTime;
        Button headerBtnSave;
        Spinner headerSpinner;

        public MyViewHolder(View view) {
            super(view);

            childRecycler = (RecyclerView) view.findViewById(R.id.ChildView) ;
            HeaderLogo = (ImageView) view.findViewById(R.id.HeaderLogo);
            HeaderRestTitle = (CustomTextView) view.findViewById(R.id.RestHeaderName);
            HeaderDescription = (CustomTextView) view.findViewById(R.id.headerdescriptionSettings);
            headerBtnSave = (Button) view.findViewById(R.id.HeaderSaveSettings);
            headerSpinner = (Spinner) view.findViewById(R.id.spinnerSettings);
            settingsStartTime = (CustomEditText) view.findViewById(R.id.settingsStartTime);
            settingsEndTime = (CustomEditText) view.findViewById(R.id.settingsEndTime);

            settingsStartTime.addTextChangedListener(new TextWatcher() {
                int len=0;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    String str = settingsStartTime.getText().toString();
                    len = str.length();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = settingsStartTime.getText().toString();
                    if(str.length()==2&& len <str.length()){//len check for backspace
                        settingsStartTime.append(":");
                    }
                }
            });

            settingsEndTime.addTextChangedListener(new TextWatcher() {
                int len=0;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    String str = settingsEndTime.getText().toString();
                    len = str.length();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = settingsEndTime.getText().toString();
                    if(str.length()==2&& len <str.length()){//len check for backspace
                        settingsEndTime.append(":");
                    }
                }
            });
        }
    }
    public SettingsAdapter(List<ListSettings> listSettings, Activity context){
        this.SettingsHeaderList = listSettings;
        this.context = context;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
