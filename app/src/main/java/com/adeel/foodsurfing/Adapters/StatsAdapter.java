package com.adeel.foodsurfing.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.adeel.foodsurfing.CustomTextView;
import com.adeel.foodsurfing.Fragments.Bookings;
import com.adeel.foodsurfing.R;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muhammad Adeel on 9/5/2017.
 */

public class StatsAdapter extends BaseExpandableListAdapter {
    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bookings bookings;
    public Boolean loggedIn = false;
    public String userid="0";
    private List<String> listtotal_price,listid,listpickup_time,listCustomerName,listrestaurant_name,listmeal_id,listmeal_title,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,list_currency,listrestaurant_address;
    private HashMap<String, List<String>> Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time,Hlistrestaurant_address;

    public StatsAdapter(Context context, List<String> listtotal_price, List<String> listCustomerName, List<String> listid, List<String> listpickup_time, List<String> listrestaurant_name, List<String> listmeal_id, List<String> listmeal_title, List<String> listquantity, List<String> listprice, List<String> listrestaurant_opening_time, List<String> listrestaurant_closing_time, HashMap<String , List<String>> Hlistmeal_id, HashMap<String , List<String>> Hlistmeal_title, HashMap<String , List<String>> Hlistquantity, HashMap<String , List<String>> Hlistprice, HashMap<String , List<String>> Hlistrestaurant_opening_time, HashMap<String , List<String>> Hlistrestaurant_closing_time, List<String> list_currency, List<String> listrestaurant_address, HashMap<String , List<String>> Hlistrestaurant_address) {
        this.context = context;
        this.listtotal_price = listtotal_price;
        this.listCustomerName = listCustomerName;
        this.listid = listid;
        this.listpickup_time = listpickup_time;
        this.listrestaurant_name = listrestaurant_name;
        this.listmeal_id = listmeal_id;
        this.listquantity = listquantity;
        this.listmeal_title = listmeal_title;
        this.listprice = listprice;
        this.listrestaurant_opening_time = listrestaurant_opening_time;
        this.listrestaurant_closing_time = listrestaurant_closing_time;
        this.listrestaurant_address = listrestaurant_address;
        this.Hlistrestaurant_address = Hlistrestaurant_address;
        this.Hlistmeal_id = Hlistmeal_id;
        this.Hlistmeal_title = Hlistmeal_title;
        this.Hlistquantity = Hlistquantity;
        this.Hlistprice = Hlistprice;
        this.Hlistrestaurant_opening_time = Hlistrestaurant_opening_time;
        this.Hlistrestaurant_closing_time = Hlistrestaurant_closing_time;
        this.list_currency = list_currency;
    }

    @Override
    public int getGroupCount() {
        return listid.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Hlistmeal_id.get(listid.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listid.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Hlistmeal_id.get(listid.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int i, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        final String id = listid.get(groupPosition);
        String CreatedDate = listpickup_time.get(groupPosition);
        String currency = list_currency.get(groupPosition);
        String TotalPrice = listtotal_price.get(groupPosition);

        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.stats_group_items,null);
        }
        CustomTextView StatsOrderDate = (CustomTextView) convertView.findViewById(R.id.StatsOrderDate);
        CustomTextView total_price = (CustomTextView) convertView.findViewById(R.id.Statsorderprice);
        ImageView ExpandableImageIcon = (ImageView) convertView.findViewById(R.id.Statsorderexpand);


        StatsOrderDate.setText(CreatedDate);
        total_price.setText(TotalPrice+" "+currency);
        int imageResourceId = isExpanded ? R.drawable.iconcollapse
                : R.drawable.iconexpand;
        ExpandableImageIcon.setImageResource(imageResourceId);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        String currency = list_currency.get(groupPosition);

        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.stats_childs,null);
        }

        CustomTextView statCreatedDate = (CustomTextView) convertView.findViewById(R.id.statCreatedDate);
        statCreatedDate.setText(listrestaurant_name.get(groupPosition));
        CustomTextView StatsUserEmail = (CustomTextView) convertView.findViewById(R.id.StatsUserEmail);
        StatsUserEmail.setText(Hlistrestaurant_address.get(listid.get(groupPosition)).get(childPosition));

        CustomTextView mealname = (CustomTextView) convertView.findViewById(R.id.Stats_mealname);
        mealname.setText(Hlistmeal_title.get(listid.get(groupPosition)).get(childPosition));
        CustomTextView Stats_quantityname = (CustomTextView) convertView.findViewById(R.id.Stats_quantityicon1);
        Stats_quantityname.setText(Hlistquantity.get(listid.get(groupPosition)).get(childPosition));

        CustomTextView Stats_amountname = (CustomTextView) convertView.findViewById(R.id.Stats_amountname);
        Stats_amountname.setText(Hlistprice.get(listid.get(groupPosition)).get(childPosition)+currency);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
