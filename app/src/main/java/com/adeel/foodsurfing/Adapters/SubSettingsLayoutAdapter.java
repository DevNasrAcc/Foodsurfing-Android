package com.adeel.foodsurfing.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.adeel.foodsurfing.AllLocations;
import com.adeel.foodsurfing.CustomEditText;
import com.adeel.foodsurfing.CustomTextView;
import com.adeel.foodsurfing.Fragments.Bookings;
import com.adeel.foodsurfing.Fragments.Settings;
import com.adeel.foodsurfing.GetSetMethod.ListSettings;
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

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad Adeel on 9/7/2017.
 */

public class SubSettingsLayoutAdapter extends RecyclerView.Adapter<SubSettingsLayoutAdapter.MyViewHolder> {

        private int[] tempcounter;

        private List<ListSettings> SettingsHeaderList;
        private List<SettingsChildResponse> settingsChildResponses;
        private static int allcounter=0;
        private static int[] likephotosetter;
        public Boolean loggedIn = false;


        int size=0;
        View viewer;
        Activity context;

        @Override
        public SubSettingsLayoutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_settings, parent, false);
            return new SubSettingsLayoutAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SubSettingsLayoutAdapter.MyViewHolder holder, final int position) {
            final SettingsChildResponse Response = settingsChildResponses.get(position);
            size = size + Settings.sizeSettingsResponse;
            viewer = holder.itemView;

                 Glide.with(context).load(Response.getMeals_image())
                        .thumbnail(0.5f)
                        .crossFade()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ChildLogo);

            holder.ChildPrice.setText(Response.getPrice());
            holder.Childtitle.setText(Response.getTitle());
            holder.Quantity.setText(Response.getQuantity());
            holder.ChildDescription.setText(Response.getRestaurant_meals_description());
            holder.currency.setText("€");
            tempcounter = new int[size];

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempcounter[position] = Integer.valueOf(holder.Quantity.getText().toString());

                    if (holder.Quantity.getText().equals("0"))
                    {
                        tempcounter[position]++;

                    } else
                    {
                        tempcounter[position]++;
                    }
                    holder.Quantity.setText(String.valueOf(tempcounter[position]));
                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempcounter[position] = Integer.valueOf(holder.Quantity.getText().toString());
                    if (tempcounter[position] > 0)
                    {
                        tempcounter[position]--;
                        holder.Quantity.setText(String.valueOf(tempcounter[position]));
                    }
                    else
                    {
                        Toast.makeText(context, "please add items first", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.ChildButtonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    UpdateMealsQuantity(Response.getRestaurant_meals_Id(),holder.Quantity.getText().toString());
                }
            });
        }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void UpdateMealsQuantity(final String mealId, final String quatity) {
        if(isOnline())
        {

            ((AllLocations)context).showprogress("Bestellungen werden gesucht...");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Publicvars.Globals1+"updateMealQuantity",
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

                                        Toast.makeText(context, "Einstellungen werden gespeichert", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(context, "Quantity not set", Toast.LENGTH_SHORT).show();

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
                    params.put("meal_id", mealId);
                    params.put("quantity", quatity);
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
            return settingsChildResponses.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView ChildLogo;
            CustomEditText Quantity;
            CustomTextView HeaderRestTitle,HeaderDescription,Childtitle,ChildDescription,ChildPrice,currency;
            Button ChildButtonSave;
            ImageButton minus,add;

            public MyViewHolder(View view) {
                super(view);


                ChildLogo = (ImageView) view.findViewById(R.id.childLogoSettings);
                ChildPrice = (CustomTextView) view.findViewById(R.id.Settprice);
                Quantity = (CustomEditText) view.findViewById(R.id.SettingsCounter);
                ChildDescription = (CustomTextView) view.findViewById(R.id.SettingsCildDesc);
                Childtitle = (CustomTextView) view.findViewById(R.id.ChildMealHeading);
                currency = (CustomTextView) view.findViewById(R.id.Settcurrency);
                //bt

                ChildButtonSave = (Button) view.findViewById(R.id.ChildSettingsSave);
                //imagebt
                minus = (ImageButton) view.findViewById(R.id.SettingsMinus);
                add = (ImageButton) view.findViewById(R.id.SettingsAdd);
            }
        }

    public SubSettingsLayoutAdapter(List<SettingsChildResponse> listSettings, Activity context){
            this.settingsChildResponses = listSettings;
            // this.SettingsHeaderList = HeaderlistRecords;
            // this.settingsChildResponses = ChildListRec;
            this.context = context;
        }
}
