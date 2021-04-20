package com.adeel.foodsurfing;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;


public class MyOrdersAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listtotal_price,listid,listpickup_time,listrestaurant_name,listmeal_id,listmeal_title,listquantity,listprice,listrestaurant_opening_time,listrestaurant_closing_time,list_currency,listrestaurant_address;
    private HashMap<String, List<String>> Hlistmeal_id,Hlistmeal_title,Hlistquantity,Hlistprice,Hlistrestaurant_opening_time,Hlistrestaurant_closing_time,Hlistrestaurant_address;

    public MyOrdersAdapter(Context context, List<String> listtotal_price,List<String> listid,List<String> listpickup_time,List<String> listrestaurant_name,List<String> listmeal_id,List<String> listmeal_title,List<String> listquantity,List<String> listprice,List<String> listrestaurant_opening_time,List<String> listrestaurant_closing_time,HashMap<String , List<String>> Hlistmeal_id,HashMap<String , List<String>> Hlistmeal_title,HashMap<String , List<String>> Hlistquantity,HashMap<String , List<String>> Hlistprice,HashMap<String , List<String>> Hlistrestaurant_opening_time,HashMap<String , List<String>> Hlistrestaurant_closing_time,List<String> list_currency,List<String> listrestaurant_address,HashMap<String , List<String>> Hlistrestaurant_address) {
        this.context = context;
        this.listtotal_price = listtotal_price;
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String subitems = (String) getChild(groupPosition,childPosition);
        String currency = list_currency.get(groupPosition);
        //Typeface bold = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Medium.otf");
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_child_my_orders,null);
        }

        CustomTextView restaurantname = (CustomTextView) convertView.findViewById(R.id.restaurantname);
        restaurantname.setText(listrestaurant_name.get(groupPosition));
        CustomTextView pickupaddress = (CustomTextView) convertView.findViewById(R.id.pickupaddress);
        pickupaddress.setText(Hlistrestaurant_address.get(listid.get(groupPosition)).get(childPosition));
        CustomTextView pickuptime = (CustomTextView) convertView.findViewById(R.id.pickuptime);
        String ct = Hlistrestaurant_closing_time.get(listid.get(groupPosition)).get(childPosition);
        String ct0 = ct.substring(0,2);
        int ct1 = Integer.valueOf(ct0);
        if(ct1-12>0)
        {
            ct1 = ct1-12;
            ct = ct1+" PM";
        }
        else
        {
            ct1 = 12-ct1;
            ct = ct1+" AM";
        }

        String ot = Hlistrestaurant_opening_time.get(listid.get(groupPosition)).get(childPosition);
        String ot0 = ot.substring(0,2);
        int ot1 = Integer.valueOf(ot0);
        if(ot1-12>0)
        {
            ot1 = ot1-12;
            ot = ot1+" PM";
        }
        else
        {
            ot1 = 12-ot1;
            ot = ot1+" AM";
        }

        pickuptime.setText(ot+" to "+ct);
        CustomTextView mealname = (CustomTextView) convertView.findViewById(R.id.mealname);
        mealname.setText(Hlistmeal_title.get(listid.get(groupPosition)).get(childPosition));
        CustomTextView quantityicon1 = (CustomTextView) convertView.findViewById(R.id.quantityicon1);
        quantityicon1.setText(Hlistquantity.get(listid.get(groupPosition)).get(childPosition));
        CustomTextView calendername = (CustomTextView) convertView.findViewById(R.id.calendername);
        calendername.setText((listpickup_time.get(groupPosition)));
        CustomTextView amountname = (CustomTextView) convertView.findViewById(R.id.amountname);
        amountname.setText(Hlistprice.get(listid.get(groupPosition)).get(childPosition)+currency);

        if(groupPosition==0&&childPosition==0)
        {
            Log.i("adeel","yes ");
           setstringcache("requirement","required",listrestaurant_name.get(groupPosition)+listid.get(groupPosition));
            restaurantname. setVisibility(View.VISIBLE);
            pickupaddress.  setVisibility(View.VISIBLE);
            pickuptime.     setVisibility(View.VISIBLE);
        }
        else
        {
            if(!getstringcache("requirement","required").equals(listrestaurant_name.get(groupPosition)+listid.get(groupPosition)))
            {
                setstringcache("requirement","required",listrestaurant_name.get(groupPosition)+listid.get(groupPosition));
                restaurantname. setVisibility(View.VISIBLE);
                pickupaddress.  setVisibility(View.VISIBLE);
                pickuptime.     setVisibility(View.VISIBLE);
            }
            else
            {
                restaurantname. setVisibility(View.GONE);
                pickupaddress.  setVisibility(View.GONE);
                pickuptime.     setVisibility(View.GONE);
            }
        }


        return convertView;
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        String orderno = listid.get(groupPosition);
        String currency = list_currency.get(groupPosition);
        String orderpr = listtotal_price.get(groupPosition);
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_group_my_orders,null);
        }
        CustomTextView ordernumber = (CustomTextView) convertView.findViewById(R.id.ordernumber);
        CustomTextView orderprice = (CustomTextView) convertView.findViewById(R.id.orderprice);
        ImageView orderexpand = (ImageView) convertView.findViewById(R.id.orderexpand);

        ordernumber.setText("# Ord-"+orderno);
        orderprice.setText(orderpr+" "+currency);
        int imageResourceId = isExpanded ? R.drawable.iconcollapse
                : R.drawable.iconexpand;
        orderexpand.setImageResource(imageResourceId);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
