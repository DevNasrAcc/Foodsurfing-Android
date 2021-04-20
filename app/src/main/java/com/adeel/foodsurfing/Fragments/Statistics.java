package com.adeel.foodsurfing.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adeel.foodsurfing.AllLocations;
import com.adeel.foodsurfing.CustomTextView;
import com.adeel.foodsurfing.Publicvars;
import com.adeel.foodsurfing.R;
import com.adeel.foodsurfing.Adapters.StatsAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muhammad Adeel on 9/5/2017.
 */

public class Statistics extends Fragment {
    boolean SearchServiceCall = false;
    CustomTextView StatsMiddleText;
    private HashMap<String, List<String>> Hlisttotal_price,Hlistid,Hlistpickup_time,Hlistrestaurant_name,Hlistrestaurant_address,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time;
    private List<String> listtotal_price,listCreatedDate,listid,listCustomerName,listpickup_time,listrestaurant_name,listmeal_id,listmeal_title,listrestaurant_address,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,list_currency;
    private ExpandableListView StatsList;
    public Boolean loggedIn = false;
    public String userid="0", ToDateStr,FromDateStr;

    ImageView FromImage, ToImage;
    TextView FromDate, ToDate;
    StatsAdapter statsAdapter;
    private int mYear, mMonth, mDay;
    Button SearchBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.stats_layout, container,false);
        ((AllLocations)getContext()).showprogress("Bestellungen werden gesucht...");
        StatsMiddleText = (CustomTextView) view.findViewById(R.id.Stats_middle);
        FromImage = (ImageView) view.findViewById(R.id.imageViewFrom);
        ToImage = (ImageView) view.findViewById(R.id.imageViewTo);
        FromDate = (TextView) view.findViewById(R.id.FromDate);
        ToDate = (TextView) view.findViewById(R.id.ToDate);
        SearchBtn = (Button) view.findViewById(R.id.SearchBtn);

        FromImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.StatsDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                if (monthOfYear < 10 && dayOfMonth < 10)
                                {
                                    monthOfYear = monthOfYear + 1;
                                    if (monthOfYear == 10){
                                        FromDateStr = year + "-" + (monthOfYear) + "-" + "0"+dayOfMonth;
                                        FromDate.setText(year + "-" + (monthOfYear) + "-" + "0"+dayOfMonth);
                                    } else {
                                        FromDateStr = year + "-" + ("0" + monthOfYear) + "-" + "0" + dayOfMonth;
                                        FromDate.setText(year + "-" + ("0" + monthOfYear) + "-" + "0" + dayOfMonth);
                                    }
                                }
                                else if (monthOfYear < 10 && dayOfMonth >= 10)
                                {
                                    monthOfYear = monthOfYear +1;
                                    if (monthOfYear == 10){
                                        FromDateStr = year + "-" + (monthOfYear) + "-" + dayOfMonth;
                                        FromDate.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                                    } else {
                                        FromDateStr = year + "-" + ("0"+monthOfYear) + "-" + dayOfMonth;
                                        FromDate.setText(year + "-" + ("0"+monthOfYear) + "-" + dayOfMonth);
                                    }
                                }
                                else
                                {
                                    monthOfYear = monthOfYear +1;
                                    FromDateStr = year + "-" + (monthOfYear) + "-" + dayOfMonth;
                                    FromDate.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ToImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.StatsDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth)
                            {
                                if (monthOfYear < 10 && dayOfMonth < 10)
                                {
                                    monthOfYear = monthOfYear + 1;
                                    if (monthOfYear == 10){
                                        FromDateStr = year + "-" + (monthOfYear) + "-" + "0"+dayOfMonth;
                                        FromDate.setText(year + "-" + (monthOfYear) + "-" + "0"+dayOfMonth);
                                    } else {
                                        FromDateStr = year + "-" + ("0" + monthOfYear) + "-" + "0" + dayOfMonth;
                                        FromDate.setText(year + "-" + ("0" + monthOfYear) + "-" + "0" + dayOfMonth);
                                    }
                                }
                                else if (monthOfYear < 10 && dayOfMonth >= 10)
                                {
                                    monthOfYear = monthOfYear +1;
                                    if (monthOfYear == 10){
                                        ToDateStr = year + "-" + (monthOfYear) + "-" + dayOfMonth;
                                        ToDate.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                                    } else {
                                        ToDateStr = year + "-" + ("0"+monthOfYear) + "-" + dayOfMonth;
                                        ToDate.setText(year + "-" + ("0"+monthOfYear) + "-" + dayOfMonth);
                                    }
                                }
                                else
                                {
                                    monthOfYear = monthOfYear +1;
                                    ToDateStr = year + "-" + (monthOfYear ) + "-" + dayOfMonth;
                                    ToDate.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchServiceCall = true;
                String startDate = FromDate.getText().toString();
                String endDate = ToDate.getText().toString();

                if (FromDate.getText().toString().toLowerCase().equals("start date")
                        && ToDate.getText().toString().toLowerCase().equals("end date")){

                    Toast.makeText(getActivity(), "Bitte ein Start- und Enddatum wählen", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    isDateAfter(startDate,endDate);

                }
            }
        });
        StatsList = (ExpandableListView) view.findViewById(R.id.Stats_list);
        StatsList.setScrollingCacheEnabled(false);
        StatsList.setFastScrollEnabled(true);

        StatsResponse();

        return view;
    }
    public boolean isDateAfter(String startDate, String endDate)
    {
        try
        {
            String myFormatString = "yyyy-mm-dd"; //
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate)) {
                SearchResponse();
                return true;
            }else {
                Toast.makeText(getContext(), "End date must be greater then start date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e)
        {

            return false;
        }
    }
    private void SearchResponse(){
        ((AllLocations)getContext()).showprogress("Bestellungen werden gesucht...");
        listid = new ArrayList<String>();
        listCustomerName = new ArrayList<String>();
        listCreatedDate = new ArrayList<String>();
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
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals3+"getRestaurantFilteredStatistics",
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
                                                String created_at = jsonObjecto.getString("created_at");
                                                String customer_email = jsonObjecto.getString("customer_email");
                                                String customer_name = jsonObjecto.getString("customer_name");
                                                String currency = jsonObjecto.getString("currency");

                                                listtotal_price.add(total_price);
                                                listid.add(id);
                                                listpickup_time.add("");
                                                listCustomerName.add("");
                                                listCreatedDate.add(created_at);

                                                if(jsonObjecto.getJSONArray("meals").length()>0)
                                                {
                                                    JSONArray meals = jsonObjecto.getJSONArray("meals");
                                                    for (int b=0;b<meals.length();b++)
                                                    {
                                                        JSONObject jsonObjectm = meals.getJSONObject(b);


                                                        String meal_title = jsonObjectm.getString("meal_title");
                                                        String quantity = jsonObjectm.getString("quantity");
                                                        String price = jsonObjectm.getString("price");

                                                        String restaurant_address = jsonObjectm.getString("restaurant_id");

                                                        list_currency.add(currency);
                                                        listrestaurant_name.add(customer_name);
                                                        listmeal_id.add("");
                                                        listmeal_title.add(meal_title);
                                                        listquantity.add(quantity);
                                                        listprice.add(price);
                                                        listrestaurant_opening_time.add("");
                                                        listrestaurant_closing_time.add("");
                                                        listrestaurant_address.add(customer_email);

                                                        temporary.add("");
                                                        temporary2.add(quantity);
                                                        temporary3.add(meal_title);
                                                        temporary4.add(price);
                                                        temporary5.add("");
                                                        temporary6.add("");
                                                        temporary7.add(customer_email);
                                                    }
                                                    Hlistmeal_id.put(listid.get(a),temporary);
                                                    Hlistquantity.put(listid.get(a),temporary2);
                                                    Hlistmeal_title.put(listid.get(a),temporary3);
                                                    Hlistprice.put(listid.get(a),temporary4);
                                                    Hlistrestaurant_closing_time.put(listid.get(a),temporary5);
                                                    Hlistrestaurant_opening_time.put(listid.get(a),temporary6);
                                                    Hlistrestaurant_address.put(listid.get(a),temporary7);
                                                    Log.i("adeel",Hlistquantity.toString());

                                                    statsAdapter =  new StatsAdapter(getContext(),listtotal_price,listCustomerName,listid,listCreatedDate,listrestaurant_name,listmeal_id,listmeal_title,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time,list_currency,listrestaurant_address,Hlistrestaurant_address);
                                                    StatsList.setAdapter(statsAdapter);
                                                    ((AllLocations)getContext()).hideprogress("");
                                                }

                                            }
                                        }
                                        else
                                        {
                                            ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                            StatsMiddleText.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                        StatsMiddleText.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    StatsList.setVisibility(View.GONE);
                                    StatsMiddleText.setVisibility(View.VISIBLE);
                                    StatsMiddleText.setText("Keine Bestellungen gefunden");
                                    ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                StatsMiddleText.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((AllLocations)getContext()).hideprogress("Failed connection server");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("user_id", userid);
                    params.put("start_date", FromDateStr);
                    params.put("end_date", ToDateStr);
                    //returning parameters
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }
        else
        {
            ((AllLocations)getContext()).hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
        }


    }
    private void StatsResponse(){
        listid = new ArrayList<String>();
        listCustomerName = new ArrayList<String>();
        listCreatedDate = new ArrayList<String>();
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
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals3+"getRestaurantStatistics",
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
                                            for (int a=0;a<orders.length();a++) {
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
                                                String created_at = jsonObjecto.getString("created_at");
                                                String customer_email = jsonObjecto.getString("customer_email");
                                                String customer_name = jsonObjecto.getString("customer_name");
                                                String currency = jsonObjecto.getString("currency");

                                                listtotal_price.add(total_price);
                                                listid.add(id);
                                                listpickup_time.add("");
                                                listCustomerName.add("");
                                                listCreatedDate.add(created_at);

                                                if(jsonObjecto.getJSONArray("meals").length()>0)
                                                {
                                                    JSONArray meals = jsonObjecto.getJSONArray("meals");
                                                    for (int b=0;b<meals.length();b++)
                                                    {
                                                        JSONObject jsonObjectm = meals.getJSONObject(b);


                                                        String meal_title = jsonObjectm.getString("meal_title");
                                                        String quantity = jsonObjectm.getString("quantity");
                                                        String price = jsonObjectm.getString("price");

                                                        String restaurant_address = jsonObjectm.getString("restaurant_id");

                                                        list_currency.add(currency);
                                                        listrestaurant_name.add(customer_name);
                                                        listmeal_id.add("");
                                                        listmeal_title.add(meal_title);
                                                        listquantity.add(quantity);
                                                        listprice.add(price);
                                                        listrestaurant_opening_time.add("");
                                                        listrestaurant_closing_time.add("");
                                                        listrestaurant_address.add(customer_email);

                                                        temporary.add("");
                                                        temporary2.add(quantity);
                                                        temporary3.add(meal_title);
                                                        temporary4.add(price);
                                                        temporary5.add("");
                                                        temporary6.add("");
                                                        temporary7.add(customer_email);
                                                    }
                                                    Hlistmeal_id.put(listid.get(a),temporary);
                                                    Hlistquantity.put(listid.get(a),temporary2);
                                                    Hlistmeal_title.put(listid.get(a),temporary3);
                                                    Hlistprice.put(listid.get(a),temporary4);
                                                    Hlistrestaurant_closing_time.put(listid.get(a),temporary5);
                                                    Hlistrestaurant_opening_time.put(listid.get(a),temporary6);
                                                    Hlistrestaurant_address.put(listid.get(a),temporary7);
//                                                    Log.i("adeel",Hlistquantity.toString());
                                                    statsAdapter =  new StatsAdapter(getContext(),listtotal_price,listCustomerName,listid,listCreatedDate,listrestaurant_name,listmeal_id,listmeal_title,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time,list_currency,listrestaurant_address,Hlistrestaurant_address);
                                                    StatsList.setAdapter(statsAdapter);
                                                    ((AllLocations)getContext()).hideprogress("");
                                                }

                                            }
                                        }
                                        else
                                        {
                                            ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                            StatsMiddleText.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                        StatsMiddleText.setVisibility(View.VISIBLE);
                                    }

                                    } catch (JSONException e) {
                                    e.printStackTrace();
                                    ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                    StatsMiddleText.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Keine Bestellungen gefunden");
                                StatsMiddleText.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((AllLocations)getContext()).hideprogress("Failed connection server");
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
