package com.adeel.foodsurfing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import java.util.Timer;

class Cartitemadapter extends RecyclerView.Adapter<Cartitemadapter.MyViewHolder>  {

    public static int tempc = 0;
    public static int tempc2 = 0;
    private int[] tempcounter;
    public float totalprice=0;
    private List<Productflower> productlist;
    private static int allcounter=0;
    private static int[] likephotosetter;
    public Boolean loggedIn = false;
    static Boolean itispressed=true;
    Timer t;
    int size=0;
    View viewer;

    Activity context;
    private List<Productflower> locationlist;
    public static int a = 0;
    private CartHandler cartHandler;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView pcover,plike;
        CustomEditText pcounter;
        CustomTextView pname,pprice,pdescription,pviewmore,pquantitydata,pcurrency, ppickuptime;
        Button pbuy;
        ImageButton pminus,padd;
        ScrollView scp;

        MyViewHolder(View view) {
            super(view);
            //imagevw
            pcurrency = (CustomTextView) view.findViewById(R.id.pcurrency);
            pcover = (ImageView) view.findViewById(R.id.pcover);
            plike = (ImageView) view.findViewById(R.id.plike);
            //text
            pname = (CustomTextView) view.findViewById(R.id.pname);
            pprice = (CustomTextView) view.findViewById(R.id.pprice);
            scp = (ScrollView) view.findViewById(R.id.scp);
            pdescription = (CustomTextView) view.findViewById(R.id.pdescription);
            ppickuptime = (CustomTextView) view.findViewById(R.id.ppickuptime);
            pviewmore = (CustomTextView) view.findViewById(R.id.pviewmore);
            pquantitydata = (CustomTextView) view.findViewById(R.id.pquantitydata);
            pcounter = (CustomEditText) view.findViewById(R.id.pcounter);
            //bt
            pbuy = (Button) view.findViewById(R.id.pbuy);
            //imagebt
            pminus = (ImageButton) view.findViewById(R.id.pminus);
            padd = (ImageButton) view.findViewById(R.id.padd);
        }
    }

    Cartitemadapter(List<Productflower> locationlist, Activity context) {
        this.locationlist = locationlist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_products, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Productflower movie = locationlist.get(position);
        cartHandler = new CartHandler(context);
        if(getstringcache("currentfrag","frag").equals("cartsm"))
        {
            size = size + FavoriteMeals.sizer;
        }
        else
        {
            size = size + ProductsTabFragment.sizer;
        }

        viewer = holder.itemView;
        Float pricer = 0F;
            pricer = (Float.valueOf(movie.getProduct_price()) * cartHandler.getProductsQuantityU(Integer.valueOf(movie.getProduct_id()),Integer.valueOf(movie.getProduct_restaurant_id())));
            totalprice = context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).getFloat("count", 0);
            totalprice = totalprice + pricer;
            context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).edit().putFloat("count",totalprice).apply();
            ((AllLocations)context).callactivity(String.valueOf(totalprice),String.valueOf(cartHandler.getallProductsCount()));

        Glide.with(context).load(movie.getProduct_image())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pcover);
        holder.pname.setText(movie.getProduct_title());
        holder.pprice.setText(movie.getProduct_price());
        holder.pcurrency.setText(movie.getProduct_currency());
        holder.pdescription.setText(movie.getProduct_description());
        holder.ppickuptime.setText(movie.getProduct_pickup());
        holder.pviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_product_details);
                CustomTextView rname = (CustomTextView) dialog.findViewById(R.id.rname);
                CustomTextView rdescription = (CustomTextView) dialog.findViewById(R.id.rdescription);
                CustomTextView close = (CustomTextView) dialog.findViewById(R.id.close);

                rname.setText(movie.getProduct_title());
                rdescription.setText(movie.getProduct_description());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        holder.pquantitydata.setText(movie.getProduct_quantity());

        holder.pdescription.setVisibility(View.GONE);
        holder.scp.setVisibility(View.GONE);
        holder.ppickuptime.setVisibility(View.GONE);

        tempcounter = new int[size];
        likephotosetter = new int[size];
        tempcounter[position]=0;

        holder.pcounter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(Integer.valueOf(textView.getText().toString())<=Integer.valueOf(movie.getProduct_quantity()))
                {
                    tempc = tempcounter[position];
                    tempc2 = Integer.valueOf(textView.getText().toString());
                    if(tempc>tempc2)
                    {
                        tempc = tempc - tempc2;
                        for(int d=0;d<tempc;d++)
                        {
                            tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));

                            tempcounter[position]--;
                            if (tempcounter[position] >= 1) {
                                cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_quantity()),tempcounter[position],Integer.parseInt(movie.getProduct_restaurant_id()),Float.valueOf(movie.getProduct_price()),movie.getProduct_title(),movie.getProduct_description(),movie.getProduct_status(),movie.getProduct_image(),movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                            } else {
                                cartHandler.deleteProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));
                            }
                            totalprice = context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).getFloat("count", 0);
                            totalprice = totalprice - Float.valueOf(movie.getProduct_price());
                            context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).edit().putFloat("count",totalprice).apply();
                            ((AllLocations)context).callactivity(String.valueOf(totalprice),String.valueOf(cartHandler.getallProductsCount()));

                            allcounter = cartHandler.getallProductsQuantity();
                        }
                    }
                    else if(tempc2>tempc)
                    {
                        tempc = tempc2 - tempc;
                        for(int c=0;c<tempc;c++)
                        {
                            if (holder.pcounter.getText().toString().equals("0")) {
                                tempcounter[position]++;
                                cartHandler.addProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                            } else {
                                tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));
                                tempcounter[position]++;
                                cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                            }
                            totalprice = context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).getFloat("count", 0);
                            totalprice = totalprice + Float.valueOf(movie.getProduct_price());
                            context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).edit().putFloat("count", totalprice).apply();
                            ((AllLocations) context).callactivity(String.valueOf(totalprice), String.valueOf(cartHandler.getallProductsCount()));

                            allcounter = cartHandler.getallProductsQuantity();
                        }
                    }
                    else
                    {
                        if(cartHandler.getProductCount(Integer.valueOf(movie.getProduct_id()),Integer.valueOf(movie.getProduct_restaurant_id()))>0) {
                            tempc = cartHandler.getProductCount(Integer.valueOf(movie.getProduct_id()),Integer.valueOf(movie.getProduct_restaurant_id()));
                            holder.pcounter.setText("0");
                            for (int d = 0; d < tempc; d++) {
                                tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));

                                tempcounter[position]--;
                                if (tempcounter[position] >= 1) {
                                    cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(), movie.getProduct_currency(),movie.getProduct_pickup()));
                                } else {
                                    cartHandler.deleteProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));
                                }
                                totalprice = context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).getFloat("count", 0);
                                totalprice = totalprice - Float.valueOf(movie.getProduct_price());
                                context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).edit().putFloat("count", totalprice).apply();
                                ((AllLocations) context).callactivity(String.valueOf(totalprice), String.valueOf(cartHandler.getallProductsCount()));

                                allcounter = cartHandler.getallProductsQuantity();
                            }
                        }
                        else
                        {
                            holder.pcounter.setText("0");
                        }
                    }
                }
                else
                {
                    holder.pcounter.setText(String.valueOf(tempcounter[position]));
                    statusbasic("Nicht mehr als "+movie.getProduct_quantity()+" artikel verfügbar",holder.itemView);
                }
                return false;
            }
        });

        final CartHandler cartHandler = new CartHandler(context);
        if(cartHandler.getProductCount(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()))>0)
        {
            tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));
            holder.pbuy.setText("Entfernen");
        }

        holder.pcounter.setText(String.valueOf(tempcounter[position]));

        holder.plike.setImageResource(R.drawable.iconunfavproduct);
        likephotosetter[position] = 0;
        context.getSharedPreferences("likephotosession", Context.MODE_PRIVATE).edit().putInt("likephoto"+position,0).apply();
        if(movie.getIs_favorite().equals("true"))//////////////////////////////////////////////////////////////////////////////////////
        {
            holder.plike.setImageResource(R.drawable.iconfavproduct);
            likephotosetter[position] = 1;
            context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).edit().putInt("likephoto"+position,1).apply();
        }

        holder.padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempcounter[position]<Integer.valueOf(movie.getProduct_quantity()))
                {
                    if (holder.pcounter.getText().toString().equals("0")) {
                        tempcounter[position]++;
                        cartHandler.addProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                    } else {
                        tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));
                        tempcounter[position]++;
                        cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                    }
                        totalprice = context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).getFloat("count", 0);
                        totalprice = totalprice + Float.valueOf(movie.getProduct_price());
                        context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).edit().putFloat("count", totalprice).apply();
                        ((AllLocations) context).callactivity(String.valueOf(totalprice), String.valueOf(cartHandler.getallProductsCount()));

                    holder.pcounter.setText(String.valueOf(tempcounter[position]));
                    allcounter = cartHandler.getallProductsQuantity();
                }
                else
                {
                    statusbasic("Nicht mehr als "+movie.getProduct_quantity()+" artikel verfügbar",holder.itemView);
                }
            }
        });

        holder.pminus.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (cartHandler.getProductCount(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id())) > 0) {
            tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));

            tempcounter[position]--;
            if (tempcounter[position] >= 1) {
                cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_quantity()),tempcounter[position],Integer.parseInt(movie.getProduct_restaurant_id()),Float.valueOf(movie.getProduct_price()),movie.getProduct_title(),movie.getProduct_description(),movie.getProduct_status(),movie.getProduct_image(),movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
            } else {
                cartHandler.deleteProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));
                //((AllLocations)context).notifier();
            }
                totalprice = context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).getFloat("count", 0);
                totalprice = totalprice - Float.valueOf(movie.getProduct_price());
                context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).edit().putFloat("count",totalprice).apply();
                ((AllLocations)context).callactivity(String.valueOf(totalprice),String.valueOf(cartHandler.getallProductsCount()));

            holder.pcounter.setText(String.valueOf(tempcounter[position]));
            allcounter = cartHandler.getallProductsQuantity();
        }
    }
});

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.pcounter.getWindowToken(), 0);
                if(!holder.pcounter.getText().toString().equals(String.valueOf(tempcounter[position])))
                {
                    tempc = tempcounter[position];
                    tempc2 = Integer.valueOf(holder.pcounter.getText().toString());
                    if(tempc>tempc2)
                    {
                        tempc = tempc - tempc2;
                        for(int d=0;d<tempc;d++)
                        {
                            tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));

                            tempcounter[position]--;
                            if (tempcounter[position] >= 1) {
                                cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_quantity()),tempcounter[position],Integer.parseInt(movie.getProduct_restaurant_id()),Float.valueOf(movie.getProduct_price()),movie.getProduct_title(),movie.getProduct_description(),movie.getProduct_status(),movie.getProduct_image(),movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                            } else {
                                cartHandler.deleteProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));
                            }
                            totalprice = context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).getFloat("count", 0);
                            totalprice = totalprice - Float.valueOf(movie.getProduct_price());
                            context.getSharedPreferences("totalprice",Context.MODE_PRIVATE).edit().putFloat("count",totalprice).apply();
                            ((AllLocations)context).callactivity(String.valueOf(totalprice),String.valueOf(cartHandler.getallProductsCount()));

                            allcounter = cartHandler.getallProductsQuantity();
                        }
                    }
                    else if(tempc2>tempc)
                    {
                        tempc = tempc2 - tempc;
                        for(int c=0;c<tempc;c++)
                        {
                            if (holder.pcounter.getText().toString().equals("0")) {
                                tempcounter[position]++;
                                cartHandler.addProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                            } else {
                                tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));
                                tempcounter[position]++;
                                cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(),movie.getProduct_currency(),movie.getProduct_pickup()));
                            }
                            totalprice = context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).getFloat("count", 0);
                            totalprice = totalprice + Float.valueOf(movie.getProduct_price());
                            context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).edit().putFloat("count", totalprice).apply();
                            ((AllLocations) context).callactivity(String.valueOf(totalprice), String.valueOf(cartHandler.getallProductsCount()));

                            allcounter = cartHandler.getallProductsQuantity();
                        }
                    }
                    else
                    {
                        if(cartHandler.getProductCount(Integer.valueOf(movie.getProduct_id()),Integer.valueOf(movie.getProduct_restaurant_id()))>0) {
                            tempc = cartHandler.getProductCount(Integer.valueOf(movie.getProduct_id()),Integer.valueOf(movie.getProduct_restaurant_id()));
                            holder.pcounter.setText("0");
                            for (int d = 0; d < tempc; d++) {
                                tempcounter[position] = cartHandler.getProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));

                                tempcounter[position]--;
                                if (tempcounter[position] >= 1) {
                                    cartHandler.updateProduct(new GetSetProducts(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_quantity()), tempcounter[position], Integer.parseInt(movie.getProduct_restaurant_id()), Float.valueOf(movie.getProduct_price()), movie.getProduct_title(), movie.getProduct_description(), movie.getProduct_status(), movie.getProduct_image(), movie.getIs_favorite(), movie.getProduct_currency(),movie.getProduct_pickup()));
                                } else {
                                    cartHandler.deleteProduct(Integer.parseInt(movie.getProduct_id()), Integer.parseInt(movie.getProduct_restaurant_id()));
                                }
                                totalprice = context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).getFloat("count", 0);
                                totalprice = totalprice - Float.valueOf(movie.getProduct_price());
                                context.getSharedPreferences("totalprice", Context.MODE_PRIVATE).edit().putFloat("count", totalprice).apply();
                                ((AllLocations) context).callactivity(String.valueOf(totalprice), String.valueOf(cartHandler.getallProductsCount()));

                                allcounter = cartHandler.getallProductsQuantity();
                            }
                        }
                        else
                        {
                            holder.pcounter.setText("0");
                        }
                    }
                }
                else
                {
                    holder.pcounter.setText(String.valueOf(tempcounter[position]));
                    statusbasic("Nicht mehr als " + movie.getProduct_quantity() + " artikel verfügbar",holder.itemView);
                }
            }
        });

        holder.plike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likephotosetter[position] = context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).getInt("likephoto"+position,0);
                loggedIn = context.getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getBoolean(Publicvars.SessionState, false);
                if (loggedIn)
                {
                    if (likephotosetter[position] == 0) {
                        sendalike(movie.getProduct_id(), "active");
                        holder.plike.setImageResource(R.drawable.iconfavproduct);
                        context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).edit().putInt("likephoto"+position,1).apply();
                    } else {
                        sendalike(movie.getProduct_id(), "remove");
                        holder.plike.setImageResource(R.drawable.iconunfavproduct);
                        context.getSharedPreferences("likephotosession",Context.MODE_PRIVATE).edit().putInt("likephoto"+position,0).apply();
                    }
                }
                else
                {
                    statusbasic("Login, um diese Funktion zu nutzen",holder.itemView);
                }
            }
        });

        holder.pbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartHandler.deleteProduct(Integer.parseInt(movie.getProduct_id()),Integer.parseInt(movie.getProduct_restaurant_id()));
                ((AllLocations)context).notifier();
            }
        });

    }

    private Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    public void statusbasic(String header,View view)
    {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(1000);
        snackbar.show();
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

    @Override
    public int getItemCount() {
        return locationlist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    private void sendalike(final String meal_id, final String status) {
        if(isOnline())
        {
            final String user = getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals1+"mealFavourite",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    if (users.equals("OK")) {

                                    }
                                    else
                                    {
                                        statusbasic("Bitte überprüfen Sie Ihre Internetverbindung",viewer);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            statusbasic("No Connection with server",viewer);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.i("adeel",meal_id+" , "+user+" , "+status);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("meal_id", meal_id);
                    params.put("user_id", user);
                    params.put("status", status);
                    //returning parameters
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }
        else
        {
            statusbasic("Bitte überprüfen Sie Ihre Internetverbindung",viewer);
        }
    }
}
