package com.adeel.foodsurfing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adeel.foodsurfing.Fragments.Bookings;
import com.adeel.foodsurfing.Fragments.Settings;
import com.adeel.foodsurfing.Fragments.Statistics;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AllLocations extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,EventListener {

    Toolbar toolbar;
    Boolean loggedIn = false;
    String[] country = {"Germany"};
    String[] region = {"Nordrhein-Westfalen"};
    String[] city = {"Aachen", "Bielefeld", "Bochum", "Bonn", "Bottrop", "Dortmund", "Duisburg", "Düsseldorf", "Essen", "Gelsenkirchen", "Hagen",
					"Hamm", "Herne", "Krefeld", "Köln", "Leverkusen", "Moers", "Mülheim an der Ruhr", "Mönchengladbach",
					"Neuss", "Oberhausen", "Paderborn", "Recklinghausen", "Remscheid", "Siegen", "Solingen", "Wuppertal"};
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    private Uri filePath;
    String Role;
    public static String paymentType,donePaymentId;

    CartFragment cartFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FrameLayout mainscreen;
    LocationCardsFragment locationCardsFragment;
    ChooseLocationFragment chooseLocationFragment;
    RestaurantDetailsFragment resturantDetailsFragment;
    UserProfileFragment userProfileFragment;
    MyOrdersFragment myOrdersFragment;
    FavoriteMeals favoriteMeals;
    HowItWorksFragment howItWorksFragment;
    PaymentFragment paymentFragment;
    Bookings bookings;
    Statistics statistics;
    Settings settingsFragment;

    Double lats,longs;

    ScrollView sc;
    RelativeLayout scc,homer,myordersr,userprofiler,favoritemealsr,logoutr,howitworksr,use,bookingsLayout, StatisticsLayoyt,SettingsLayout;

    CustomTextView status, hometext,bookingsText,SettingsText,StatsText, myorderstext, userprofiletext, favoritemealstext, howitworkstext, logouttext, invitefriends, follow, services, websiteFoodsurfing, mailTo, pdescription, ppickuptime;
    ImageView homeicon, myordersicon,SettingsIcon,SettingsLine,bookingline,bookingsicon,StatsLine,StatsIcon,userprofileicon, favoritemealsicon, howitworksicon, logouticon,line1,line2,line3,line4,line5,line6;
    private DrawerLayout drawer;
    private final int MY_PERMISSIONS_REQUEST_ACCESSLOCATION=12; //as you like
    public static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    RelativeLayout progresslayout;
    CustomTextView progressstatus;
    ProgressBar one,two;
    TextView thecounter;

    boolean doubleBackToExitPressedOnce = false;

    /**
     * @param savedInstanceState
     */
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme1a);
        setContentView(R.layout.activity_all_locations);
        setstringcache("currentfrag","frag","");
        setstringcache("currentfragtab", "frag", "");
        setstringcache("currentfragdetail", "frag", "");
        setbooleancache("cartchecker","shown",false);
        setbooleancache("cartchecker","showner",false);
        setstringcache("payer","price","");
        setstringcache("payer","quantity","");
        //setstringcache("requirement","required","no");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        CartHandler cartHandler = new CartHandler(getApplicationContext());

        loggedIn = getbooleancache(Publicvars.UserSession, Publicvars.SessionState);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.drawable.iconmenu);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });

        use = (RelativeLayout) findViewById(R.id.use);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        status = (CustomTextView) navigationView.findViewById(R.id.status);

        sc = (ScrollView) navigationView.findViewById(R.id.sc);
        scc = (RelativeLayout) sc.findViewById(R.id.scc);

        SharedPreferences sharedPreferences = getSharedPreferences(Publicvars.UserSession,MODE_PRIVATE);

        homer = (RelativeLayout) scc.findViewById(R.id.home);
        hometext = (CustomTextView) homer.findViewById(R.id.hometext);
        homeicon = (ImageView) homer.findViewById(R.id.homeicon);
        line1 = (ImageView) homer.findViewById(R.id.line1);

        favoritemealsr = (RelativeLayout) scc.findViewById(R.id.favoritemeals);
        favoritemealstext = (CustomTextView) favoritemealsr.findViewById(R.id.favoritemealstext);
        favoritemealsicon = (ImageView) favoritemealsr.findViewById(R.id.favoritemealsicon);
        line4 = (ImageView) favoritemealsr.findViewById(R.id.line4);

        howitworksr = (RelativeLayout) scc.findViewById(R.id.howitworks);
        howitworkstext = (CustomTextView) howitworksr.findViewById(R.id.howitworkstext);
        howitworksicon = (ImageView) howitworksr.findViewById(R.id.howitworksicon);
        line5 = (ImageView) howitworksr.findViewById(R.id.line5);

        userprofiler = (RelativeLayout) scc.findViewById(R.id.userprofile);
        userprofiletext = (CustomTextView) userprofiler.findViewById(R.id.userprofiletext);
        userprofileicon = (ImageView) userprofiler.findViewById(R.id.userprofileicon);
        line3 = (ImageView) userprofiler.findViewById(R.id.line3);

        logoutr = (RelativeLayout) scc.findViewById(R.id.logout);
        logouttext = (CustomTextView) logoutr.findViewById(R.id.logouttext);
        logouticon = (ImageView) logoutr.findViewById(R.id.logouticon);
        line6 = (ImageView) logoutr.findViewById(R.id.line6);

        myordersr = (RelativeLayout) scc.findViewById(R.id.myorders);
        myorderstext = (CustomTextView) myordersr.findViewById(R.id.myorderstext);
        myordersicon = (ImageView) myordersr.findViewById(R.id.myordersicon);
        line2 = (ImageView) myordersr.findViewById(R.id.line2);

        bookingsLayout = (RelativeLayout) scc.findViewById(R.id.bookingsLayout);
        bookingsText = (CustomTextView) bookingsLayout.findViewById(R.id.bookingsText);
        bookingsicon = (ImageView) bookingsLayout.findViewById(R.id.bookingsIcon);
        bookingline = (ImageView) bookingsLayout.findViewById(R.id.bookingLine);

        StatisticsLayoyt = (RelativeLayout) scc.findViewById(R.id.StatsLayout);
        StatsText = (CustomTextView) StatisticsLayoyt.findViewById(R.id.StatsText);
        StatsIcon = (ImageView) StatisticsLayoyt.findViewById(R.id.StatsIcon);
        StatsLine = (ImageView) StatisticsLayoyt.findViewById(R.id.StatsLine);

        SettingsLayout = (RelativeLayout) scc.findViewById(R.id.SettingsLayout);
        SettingsText = (CustomTextView) SettingsLayout.findViewById(R.id.SettingsText);
        SettingsIcon = (ImageView)SettingsLayout.findViewById(R.id.SettingsIcon);
        SettingsLine = (ImageView)SettingsLayout.findViewById(R.id.SettingsLine);

        invitefriends = (CustomTextView) navigationView.findViewById(R.id.invitefriends);
        follow = (CustomTextView) navigationView.findViewById(R.id.follow);
        websiteFoodsurfing = (CustomTextView) navigationView.findViewById(R.id.websiteFoodsurfing);
        mailTo = (CustomTextView) navigationView.findViewById(R.id.mailTo);
        services = (CustomTextView) navigationView.findViewById(R.id.services);

        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);

        pdescription = (CustomTextView) findViewById(R.id.pdescription);
        ppickuptime = (CustomTextView) findViewById(R.id.ppickuptime);

        checklocationpermission();      //check location permission first

        mainscreen = (FrameLayout) findViewById(R.id.con);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (loggedIn)
        {
            status.setVisibility(View.GONE);
            logoutr.setVisibility(View.VISIBLE);
        }
        else
        {
            status.setVisibility(View.VISIBLE);
            logoutr.setVisibility(View.GONE);
            bookingsLayout.setVisibility(View.GONE);
            SettingsLayout.setVisibility(View.GONE);
            StatisticsLayoyt.setVisibility(View.GONE);
            userprofiler.setVisibility(View.GONE);
        }

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllLocations.this, Login_SignUp.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/Foodsurfers/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        websiteFoodsurfing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Uri uri = Uri.parse("http://foodsurfing.eu/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            }
        });

        mailTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Uri uri = Uri.parse("http://www.foodsurfing.eu/contact");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            }
        });

        invitefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                try {
                    Intent shareToWhatsApp = new Intent(Intent.ACTION_SEND);
                    shareToWhatsApp.setType("text/plain");

                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                    shareToWhatsApp.setClassName("com.whatsapp", "com.whatsapp.ContactPicker");
                    shareToWhatsApp.setPackage("com.whatsapp");

                    shareToWhatsApp.putExtra(Intent.EXTRA_SUBJECT, "Food Surfing");
                    shareToWhatsApp.putExtra(
                        android.content.Intent.EXTRA_TEXT,
                        "\nLet me recommend you Food Surfing application\n\n" +
                        "Download it from here: https://play.google.com/store/apps/details?id=com.adeel.foodsurfing&hl=de \n\n"
                    );

                    startActivity(Intent.createChooser(shareToWhatsApp, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Please install Whatsapp on your device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AllLocations.this,R.style.MyDialogTheme).setTitle("Abmelden")
                        .setMessage("Sind Sie sicher?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                drawer.closeDrawers();
                                clearApplicationData();
                                logoutuser();

                                startActivity(new Intent(AllLocations.this, Login_SignUp.class));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        SettingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                SettingsText.setTextColor(Color.parseColor("#d71a6A"));
                SettingsIcon.setImageResource(R.drawable.iconhowitworken);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                StatsLine.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                myorderstext.setTextColor(Color.parseColor("#333333"));
                StatsText.setTextColor(Color.parseColor("#333333"));
                bookingsText.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                bookingsicon.setImageResource(R.mipmap.bookings);
                StatsIcon.setImageResource(R.mipmap.statistics);

                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("settings"))
                {
                    if(loggedIn) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        settingsFragment = new Settings();
                        fragmentTransaction.replace(R.id.con, settingsFragment,"settings");
                        fragmentTransaction.commit();
                        setheader("Heutige Portionen");
                        setstringcache("currentfrag", "frag", "settings");
                        invalidateOptionsMenu();
                    }
                    else
                    {
                        status.performClick();
                    }
                }
            }
        });

        StatisticsLayoyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                StatsText.setTextColor(Color.parseColor("#d71a6A"));
                StatsIcon.setImageResource(R.mipmap.statisticsen);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                StatsLine.setVisibility(View.VISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                myorderstext.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                bookingsText.setTextColor(Color.parseColor("#333333"));
                bookingsicon.setImageResource(R.mipmap.bookings);
                SettingsIcon.setImageResource(R.drawable.iconhowitwork);


                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("statistics"))
                {
                    if(loggedIn) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        statistics = new Statistics();
                        fragmentTransaction.replace(R.id.con, statistics,"statistics");
                        fragmentTransaction.commit();
                        setheader("Statistiken");
                        setstringcache("currentfrag", "frag", "statistics ");
                        invalidateOptionsMenu();
                    }
                    else
                    {
                        status.performClick();
                    }
                }
            }
        });

        bookingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                bookingsText.setTextColor(Color.parseColor("#d71a6A"));
                bookingsicon.setImageResource(R.mipmap.bookingsen);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                StatsLine.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                myorderstext.setTextColor(Color.parseColor("#333333"));
                StatsText.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                StatsIcon.setImageResource(R.mipmap.statistics);
                SettingsIcon.setImageResource(R.drawable.iconhowitwork);

                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("bookings"))
                {
                    if(loggedIn) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        bookings = new Bookings();
                        fragmentTransaction.replace(R.id.con, bookings, "bookings");
                        fragmentTransaction.commit();
                        setheader("Heutige Bestellungen");
                        setstringcache("currentfrag", "frag", "bookings ");
                        invalidateOptionsMenu();
                    }
                    else
                    {
                        status.performClick();
                    }
                }
            }
        });

        myordersr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                myorderstext.setTextColor(Color.parseColor("#d71a6A"));
                myordersicon.setImageResource(R.drawable.iconorderen);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                StatsText.setTextColor(Color.parseColor("#333333"));
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                bookingsText.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                //bookingsicon.setImageResource(R.drawable.iconorderen);

                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("myorders"))
                {
                    if(loggedIn) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        myOrdersFragment = new MyOrdersFragment();
                        fragmentTransaction.replace(R.id.con, myOrdersFragment, "myorders");
                        fragmentTransaction.commit();
                        setheader("Meine Bestellungen");
                        setstringcache("currentfrag", "frag", "myorders");
                        invalidateOptionsMenu();
                    }
                    else
                    {
                        status.performClick();
                    }
                }
            }
        });

        homer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                myorderstext.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                line2.setVisibility(View.INVISIBLE);
                line1.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                StatsText.setTextColor(Color.parseColor("#333333"));
                hometext.setTextColor(Color.parseColor("#d71a6A"));
                homeicon.setImageResource(R.drawable.iconhomeen);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                bookingsText.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                bookingsicon.setImageResource(R.mipmap.bookings);
                StatsIcon.setImageResource(R.mipmap.statistics);
                SettingsIcon.setImageResource(R.drawable.iconhowitwork);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (getstringcache(Publicvars.My_Location, Publicvars.My_latitude).equals("") || getstringcache(Publicvars.My_Location, Publicvars.My_latitude).equals("clear"))
                {
                    chooseLocationFragment = new ChooseLocationFragment();
                    fragmentTransaction.replace(R.id.con, chooseLocationFragment, "chooselocation");
                    fragmentTransaction.commit();
                    setstringcache("currentfrag", "frag", "chooselocation");
                }
                else
                {
                    locationCardsFragment = new LocationCardsFragment();
                    fragmentTransaction.replace(R.id.con, locationCardsFragment, "locationcards");
                    fragmentTransaction.commit();
                    setheader("Gastronomiebetriebe");
                    setstringcache("currentfrag", "frag", "locationcards");
                    invalidateOptionsMenu();
                }

//                if (!getstringcache("currentfrag", "frag").equals("locationcards")) {
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    locationCardsFragment = new LocationCardsFragment();
//                    fragmentTransaction.replace(R.id.con, locationCardsFragment, "locationcards");
//                    fragmentTransaction.commit();
//                    setheader("Gastronomiebetriebe");
//                    setstringcache("currentfrag", "frag", "locationcards");
//                    invalidateOptionsMenu();
//                }
            }
        });

        userprofiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                myorderstext.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                StatsLine.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                StatsText.setTextColor(Color.parseColor("#333333"));
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#d71a6A"));
                userprofileicon.setImageResource(R.drawable.iconuseren);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                bookingsText.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                bookingsicon.setImageResource(R.mipmap.bookings);
                StatsIcon.setImageResource(R.mipmap.statistics);
                SettingsIcon.setImageResource(R.drawable.iconhowitwork);

                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("userprofile"))
                {
                    if(loggedIn)
                    {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        userProfileFragment = new UserProfileFragment();
                        fragmentTransaction.replace(R.id.con,userProfileFragment, "userprofile");
                        fragmentTransaction.commit();
                        setheader("Benutzer Profil");
                        setstringcache("currentfrag","frag","userprofile");
                        invalidateOptionsMenu();
                    }
                    else
                    {
                        status.performClick();
                    }
                }
            }
        });

        favoritemealsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                myorderstext.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                line1.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.VISIBLE);
                line5.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                StatsText.setTextColor(Color.parseColor("#333333"));
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#d71a6A"));
                favoritemealsicon.setImageResource(R.drawable.iconfavoriteen);
                howitworkstext.setTextColor(Color.parseColor("#333333"));
                howitworksicon.setImageResource(R.drawable.iconhowitwork);
                bookingsText.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                // bookingsicon.setImageResource(R.drawable.iconorderen);

                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("favoritemeals"))
                {
                    if(loggedIn)
                    {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        favoriteMeals = new FavoriteMeals();
                        fragmentTransaction.replace(R.id.con,favoriteMeals, "favoritemeals");
                        fragmentTransaction.commit();
                        setheader("Lieblingsessen");
                        setstringcache("currentfrag","frag", "favoritemeals");
                        invalidateOptionsMenu();
                    }
                    else
                    {
                        status.performClick();
                    }
                }
            }
        });

        howitworksr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                myorderstext.setTextColor(Color.parseColor("#333333"));
                myordersicon.setImageResource(R.drawable.iconorder);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                line1.setVisibility(View.INVISIBLE);
                line5.setVisibility(View.VISIBLE);
                line4.setVisibility(View.INVISIBLE);
                SettingsLine.setVisibility(View.INVISIBLE);
                bookingline.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                StatsText.setTextColor(Color.parseColor("#333333"));
                hometext.setTextColor(Color.parseColor("#333333"));
                homeicon.setImageResource(R.drawable.iconhome);
                userprofiletext.setTextColor(Color.parseColor("#333333"));
                userprofileicon.setImageResource(R.drawable.iconuser);
                favoritemealstext.setTextColor(Color.parseColor("#333333"));
                favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                howitworkstext.setTextColor(Color.parseColor("#d71a6A"));
                howitworksicon.setImageResource(R.drawable.iconhowitworken);
                bookingsText.setTextColor(Color.parseColor("#333333"));
                SettingsText.setTextColor(Color.parseColor("#333333"));
                // bookingsicon.setImageResource(R.drawable.iconorderen);

//                fragmentManager = getSupportFragmentManager();
                if(!getstringcache("currentfrag","frag").equals("howitworks"))
                {
                    startActivity(new Intent(AllLocations.this, HowItWorksActivity.class));
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        howItWorksFragment = new HowItWorksFragment();
//                        fragmentTransaction.replace(R.id.con,howItWorksFragment,"howitworks");
//                        fragmentTransaction.commit();
                    setheader("So funktionert’s");
                    setstringcache("currentfrag","frag", "howitworks");
                    invalidateOptionsMenu();
                }
            }
        });

        Role = sharedPreferences.getString(Publicvars.KEY_ROLE,"0");
        if (Role.equals("2"))
        {
            favoritemealsr.setVisibility(View.GONE);
            howitworksr.setVisibility(View.GONE);
            myordersr.setVisibility(View.GONE);
        }
        else if (Role.equals("3")){
            bookingsLayout.setVisibility(View.GONE);
            SettingsLayout.setVisibility(View.GONE);
            StatisticsLayoyt.setVisibility(View.GONE);
        }

        // choose your location layout
        if (getstringcache(Publicvars.My_Location, Publicvars.My_latitude).equals("") || getstringcache(Publicvars.My_Location, Publicvars.My_latitude).equals("clear"))
        {
            if (Role.equals("2")) {
                homer.setVisibility(View.GONE);
                bookings = new Bookings();
                locationCardsFragment = new LocationCardsFragment();
                fragmentTransaction.add(R.id.con,bookings,"bookings");
                fragmentTransaction.commit();
                setheader("Today Bookings");
                setstringcache("currentfrag","frag","bookings");
                drawer.closeDrawers();
                bookingsText.setTextColor(Color.parseColor("#d71a6A"));
                bookingsicon.setImageResource(R.mipmap.bookingsen);
                bookingline.setVisibility(View.VISIBLE);
            }
            else {
                chooseLocationFragment = new ChooseLocationFragment();
                fragmentTransaction.add(R.id.con, chooseLocationFragment, "chooselocation");
                fragmentTransaction.commit();
                setstringcache("currentfrag", "frag", "chooselocation");
            }
        }
        else
        {
            if (Role.equals("2")) {
                homer.setVisibility(View.GONE);
                bookings = new Bookings();
                locationCardsFragment = new LocationCardsFragment();
                fragmentTransaction.add(R.id.con,bookings,"bookings");
                fragmentTransaction.commit();
                setheader("Today Bookings");
                setstringcache("currentfrag","frag","bookings");
                drawer.closeDrawers();
                bookingsText.setTextColor(Color.parseColor("#d71a6A"));
                bookingsicon.setImageResource(R.mipmap.bookingsen);
                bookingline.setVisibility(View.VISIBLE);
            }
            else {
                locationCardsFragment = new LocationCardsFragment();
                fragmentTransaction.add(R.id.con, locationCardsFragment, "locationcards");
                fragmentTransaction.commit();
                setheader("Gastronomiebetriebe");
                setstringcache("currentfrag", "frag", "locationcards");
                hideprogress("");
            }
        }
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem filter = menu.findItem(R.id.filter);
        final MenuItem restaurantdetails = menu.findItem(R.id.restaurantdetails);
        final View count = restaurantdetails.getActionView();
        thecounter = (TextView) count.findViewById(R.id.cont);
        ImageButton cont0 = (ImageButton) count.findViewById(R.id.cont0);
        int ac = new CartHandler(getApplicationContext()).getallProductsCount();
        if(ac!=0)
        {
            thecounter.setVisibility(View.VISIBLE);
            thecounter.setText(String.valueOf(ac));
        }
        else
        {
            thecounter.setVisibility(View.GONE);
        }
        filter.setIcon(R.drawable.iconfilter);
//        pdescription.setVisibility(View.GONE);
//        ppickuptime.setVisibility(View.GONE);
        cont0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count.performClick();
            }
        });
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getstringcache("currentfrag", "frag").equals("cartsm")||!getstringcache("currentfrag", "frag").equals("cartsr"))
                {
                    if (getstringcache("currentfrag", "frag").equals("favoritemeals"))
                    {
                        setstringcache("currentfrag","frag","cartsm");
                    }
                    else if (getstringcache("currentfrag", "frag").equals("restaurantdetails"))
                    {
                        setstringcache("currentfrag","frag","cartsr");
                    }
                    else
                    {
                        setstringcache("currentfrag","frag","cartsp");
                    }
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    cartFragment = new CartFragment();
                    fragmentTransaction.replace(R.id.con,cartFragment.newInstance(),"carat");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setheader("Warenkorb");
                    invalidateOptionsMenu();
                }
            }
        });
        //restaurantdetails.setIcon(R.drawable.iconcart);

        filter.setVisible(false);
        restaurantdetails.setVisible(false);

        if (getstringcache("currentfrag","frag").equals("restaurantdetails")||getstringcache("currentfrag","frag").equals("payment")/*||getstringcache("currentfrag","frag").equals("favoritemeals")*/)
        {
            filter.setVisible(false);
            restaurantdetails.setVisible(true);
        }
        else if (getstringcache("currentfrag","frag").equals("locationcards"))
        {
            filter.setVisible(true);
            restaurantdetails.setVisible(true);
        }
        else
        {
            filter.setVisible(false);
            restaurantdetails.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(getstringcache("currentfrag", "frag").equals("restaurantdetails"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            locationCardsFragment = new LocationCardsFragment();
            fragmentTransaction.replace(R.id.con, locationCardsFragment.newInstance(),"locationcards");
            fragmentTransaction.commit();
            setstringcache("currentfrag", "frag", "locationcards");
            setheader("Gastronomiebetriebe");
            invalidateOptionsMenu();
        }
        else if(getstringcache("currentfrag", "frag").equals("cartsm") || getstringcache("currentfrag", "frag").equals("favoritemeals_restaurant"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            favoriteMeals = new FavoriteMeals();
            fragmentTransaction.replace(R.id.con, favoriteMeals.newInstance(),"favoritemeals");
            fragmentTransaction.commit();

            if (!getstringcache("currentfrag", "frag").equals("favoritemeals_restaurant")) {
                setstringcache("currentfrag", "frag", "favoritemeals");
            }

            setheader("Lieblingsessen");
            invalidateOptionsMenu();
        }
        else if(getstringcache("currentfrag", "frag").equals("cartsp"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            locationCardsFragment = new LocationCardsFragment();
            fragmentTransaction.replace(R.id.con, locationCardsFragment.newInstance(),"locationcards");
            fragmentTransaction.commit();
            setstringcache("currentfrag", "frag", "locationcards");
            setheader("Gastronomiebetriebe");
            invalidateOptionsMenu();
        }
        else if(getstringcache("currentfrag", "frag").equals("cartsr"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            resturantDetailsFragment = new RestaurantDetailsFragment();
            fragmentTransaction.replace(R.id.con, resturantDetailsFragment.newInstance(getstringcache("restaurantresume","other1"), getstringcache("restaurantresume","otherurl"),getstringcache("restaurantresume","otherurl1"), getstringcache("restaurantresume","otherurl11"), getstringcache("restaurantresume","otherurl111")),"restaurantdetails");
            fragmentTransaction.commit();
            setheader(getstringcache("restaurantresume","other1"));
            setstringcache("currentfrag", "frag", "restaurantdetails");
            invalidateOptionsMenu();
        }
        else
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            locationCardsFragment = new LocationCardsFragment();
            fragmentTransaction.replace(R.id.con, locationCardsFragment.newInstance(),"locationcards");
            fragmentTransaction.commit();
            setstringcache("currentfrag", "frag", "locationcards");
            setheader("Gastronomiebetriebe");
            invalidateOptionsMenu();
        }

        if (getstringcache("currentfragdetail", "frag").equals("restaurantdetails_tab") || getstringcache("currentfrag", "frag").equals("favoritemeals_restaurant")
        || getstringcache("currentfrag", "frag").equals("restaurantdetails")) {
            setstringcache("currentfragdetail", "frag", "");
            setstringcache("currentfrag", "frag", "locationcards");

            return;
        }

        if (getstringcache("currentfrag", "frag").equals("locationcards") || getstringcache("currentfrag", "frag").equals("howitworks")
        || getstringcache("currentfrag", "frag").equals("settings") || !getstringcache("currentfrag", "frag").equals("cartsp")
        || getstringcache("currentfrag", "frag").equals("statistics") || getstringcache("currentfrag", "frag").equals("bookings")
        || getstringcache("currentfrag", "frag").equals("myorders") || getstringcache("currentfrag", "frag").equals("userprofile")
        || getstringcache("currentfrag", "frag").equals("favoritemeals")) {

            try {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Für Zurück bitte nochmal klicken!", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            return true;
        }
        if (id == R.id.filter) {
            final Dialog dialog = new Dialog(AllLocations.this, R.style.Theme_Filter_Dialog);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_filter);
            ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
            final RangeSeekBar distanceseek = (RangeSeekBar) dialog.findViewById(R.id.distanceseek);
            final CustomTextView distancetextmin = (CustomTextView) dialog.findViewById(R.id.distancetext1);
            final CustomTextView distancetextmax = (CustomTextView) dialog.findViewById(R.id.distancetext2);
            final RangeSeekBar timeseek = (RangeSeekBar) dialog.findViewById(R.id.timeseek);
            final CustomTextView timetextmin = (CustomTextView) dialog.findViewById(R.id.timetext1);
            final CustomTextView timetextmax = (CustomTextView) dialog.findViewById(R.id.timetext2);
            final CustomTextView apply = (CustomTextView) dialog.findViewById(R.id.apply);
            CustomTextView currenttext = (CustomTextView) dialog.findViewById(R.id.currenttext);
            final CheckBox currentcheck = (CheckBox) dialog.findViewById(R.id.currentcheck);
            final Spinner countryspinner = (Spinner) dialog.findViewById(R.id.countryspinner);
            final CustomTextView countrytext = (CustomTextView) dialog.findViewById(R.id.countrytext);
            final Spinner cityspinner = (Spinner) dialog.findViewById(R.id.cityspinner);
            final CustomTextView citytext = (CustomTextView) dialog.findViewById(R.id.citytext);
            final Spinner regionspinner = (Spinner) dialog.findViewById(R.id.regionspinner);
            final CustomTextView regiontext = (CustomTextView) dialog.findViewById(R.id.regiontext);
            final Spinneradapter2 st1 = new Spinneradapter2(getApplicationContext(),country);
            countryspinner.setAdapter(st1);
            countryspinner.setSelection(0);
            final Spinneradapter2 st2 = new Spinneradapter2(getApplicationContext(),city);
            cityspinner.setAdapter(st2);
            cityspinner.setSelection(0);
            final Spinneradapter2 st3 = new Spinneradapter2(getApplicationContext(),region);
            regionspinner.setAdapter(st3);
            regionspinner.setSelection(0);

            if(getbooleancache(Publicvars.My_Location,Publicvars.My_Check))
            {
                currentcheck.setChecked(true);
                countryspinner.setVisibility(View.GONE);
                countrytext.setVisibility(View.GONE);
                cityspinner.setVisibility(View.GONE);
                citytext.setVisibility(View.GONE);
                regionspinner.setVisibility(View.GONE);
                regiontext.setVisibility(View.GONE);
            }
            else
            {
                currentcheck.setChecked(false);
                countryspinner.setVisibility(View.VISIBLE);
                countrytext.setVisibility(View.VISIBLE);
                cityspinner.setVisibility(View.VISIBLE);
                citytext.setVisibility(View.VISIBLE);
                regionspinner.setVisibility(View.VISIBLE);
                regiontext.setVisibility(View.VISIBLE);
                if(getintcache(Publicvars.My_Location,Publicvars.My_City)!=0||
                        getintcache(Publicvars.My_Location,Publicvars.My_Region)!=0||
                        getintcache(Publicvars.My_Location,Publicvars.My_Country)!=0)
                {
                    countryspinner.setSelection(getintcache(Publicvars.My_Location,Publicvars.My_Country));
                    cityspinner.setSelection(getintcache(Publicvars.My_Location,Publicvars.My_City));
                    regionspinner.setSelection(getintcache(Publicvars.My_Location,Publicvars.My_Region));
                }
            }

            if(getintcache(Publicvars.My_Location,"firsttime")==0)
            {
                setintcache(Publicvars.My_Location,Publicvars.My_TimeMin,0);
                setintcache(Publicvars.My_Location,Publicvars.My_TimeMax,24);
                setintcache(Publicvars.My_Location,"firsttime",1);
                currentcheck.setChecked(false);
            }

            distanceseek.setRangeValues(-1, 100);
            distanceseek.setSelectedMinValue(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMin));
            distanceseek.setSelectedMaxValue(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMax));

            timeseek.setRangeValues(0, 25);
            timeseek.setSelectedMinValue(getintcache(Publicvars.My_Location,Publicvars.My_TimeMin));
            timeseek.setSelectedMaxValue(getintcache(Publicvars.My_Location,Publicvars.My_TimeMax));
            distancetextmin.setText(""+distanceseek.getSelectedMinValue());
            distancetextmax.setText(""+distanceseek.getSelectedMaxValue());
            timetextmin.setText(""+timeseek.getSelectedMinValue()+":00");
            timetextmax.setText(""+timeseek.getSelectedMaxValue()+":00");

            currenttext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentcheck.performClick();
                }
            });

            new Thread() {
                @Override
                public void run() {
                    currentcheck.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick (View view){
                              if (currentcheck.isChecked()) {
                                  countryspinner.setVisibility(View.GONE);
                                  countrytext.setVisibility(View.GONE);
                                  cityspinner.setVisibility(View.GONE);
                                  citytext.setVisibility(View.GONE);
                                  regionspinner.setVisibility(View.GONE);
                                  regiontext.setVisibility(View.GONE);
                              } else {
                                  countryspinner.setVisibility(View.VISIBLE);
                                  countrytext.setVisibility(View.VISIBLE);
                                  cityspinner.setVisibility(View.VISIBLE);
                                  citytext.setVisibility(View.VISIBLE);
                                  regionspinner.setVisibility(View.VISIBLE);
                                  regiontext.setVisibility(View.VISIBLE);
                              }
                          }
                    });
                }
            }.start();

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            new Thread() {
                @Override
                public void run() {
                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (currentcheck.isChecked()) {
                                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                                if (gpsTracker.getLatitude() > 0) {
                                    lats = gpsTracker.getLatitude();
                                    longs = gpsTracker.getLongitude();
                                    setbooleancache(Publicvars.My_Location, Publicvars.My_Check, true);
                                } else {
                                    statusbasic(use, "Kein Ort gefunden");
                                    setbooleancache(Publicvars.My_Location, Publicvars.My_Check, false);
                                }
                            } else {
                                Geocoder gc = new Geocoder(getApplicationContext());
                                if (gc.isPresent()) {
                                    try {
                                        List<Address> list = gc.getFromLocationName(city[cityspinner.getSelectedItemPosition()] + " , " + region[regionspinner.getSelectedItemPosition()] + " , " + country[countryspinner.getSelectedItemPosition()], 1);
                                        if (list.size() > 0) {
                                            Address address = list.get(0);
                                            lats = address.getLatitude();
                                            longs = address.getLongitude();
                                            setbooleancache(Publicvars.My_Location, Publicvars.My_Check, false);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            if (getintcache(Publicvars.My_Location, Publicvars.My_DistanceMin) != distanceseek.getSelectedMinValue().intValue() ||
                                    getintcache(Publicvars.My_Location, Publicvars.My_DistanceMax) != distanceseek.getSelectedMaxValue().intValue() ||
                                    getintcache(Publicvars.My_Location, Publicvars.My_TimeMin) != timeseek.getSelectedMinValue().intValue() ||
                                    getintcache(Publicvars.My_Location, Publicvars.My_TimeMax) != timeseek.getSelectedMaxValue().intValue() ||
                                    !getstringcache(Publicvars.My_Location, Publicvars.My_longitude).equals(String.valueOf(longs)) ||
                                    !getstringcache(Publicvars.My_Location, Publicvars.My_latitude).equals(String.valueOf(lats))) {
                                showprogress("Gastronomiebetriebe werden gesucht….");
                                setintcache(Publicvars.My_Location, Publicvars.My_DistanceMin, distanceseek.getSelectedMinValue().intValue());
                                setintcache(Publicvars.My_Location, Publicvars.My_DistanceMax, distanceseek.getSelectedMaxValue().intValue());
                                setintcache(Publicvars.My_Location, Publicvars.My_TimeMin, timeseek.getSelectedMinValue().intValue());
                                setintcache(Publicvars.My_Location, Publicvars.My_TimeMax, timeseek.getSelectedMaxValue().intValue());
                                setstringcache(Publicvars.My_Location, Publicvars.My_latitude, String.valueOf(lats));
                                setstringcache(Publicvars.My_Location, Publicvars.My_longitude, String.valueOf(longs));
                                setintcache(Publicvars.My_Location, Publicvars.My_City, cityspinner.getSelectedItemPosition());
                                setintcache(Publicvars.My_Location, Publicvars.My_Country, countryspinner.getSelectedItemPosition());
                                setintcache(Publicvars.My_Location, Publicvars.My_Region, regionspinner.getSelectedItemPosition());

                                fragmentManager = getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                locationCardsFragment = new LocationCardsFragment();
                                fragmentTransaction.replace(R.id.con, locationCardsFragment.newInstance());
                                setstringcache("currentfrag", "frag", "locationcards");
                                fragmentTransaction.commit();

                            }
                            dialog.cancel();
                        }
                    });
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    distanceseek.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                        @Override
                        public void onRangeSeekBarValuesChanged(final RangeSeekBar rangeSeekBar, Integer minValue, Integer maxValue) {
                            if (minValue < 0) {
                                rangeSeekBar.setEnabled(false);

                                AlertDialog.Builder alert = new AlertDialog.Builder(AllLocations.this, R.style.MyDialogTheme);
                                alert.setNegativeButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                        rangeSeekBar.setEnabled(true);
                                    }
                                });
                                alert.setCancelable(false);
//                        alert.setMessage(Html.fromHtml("Distance range is possible in between 0km - 100km")).show();

                                rangeSeekBar.setSelectedMinValue(0);
                            }

                            distancetextmin.setText("" + rangeSeekBar.getSelectedMinValue());
                            distancetextmax.setText("" + rangeSeekBar.getSelectedMaxValue());
                        }
                    });
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    timeseek.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                        @Override
                        public void onRangeSeekBarValuesChanged(final RangeSeekBar rangeSeekBar, Integer minValue, Integer maxValue) {
                            if (maxValue > 24) {
                                rangeSeekBar.setEnabled(false);

                                AlertDialog.Builder alert = new AlertDialog.Builder(AllLocations.this, R.style.MyDialogTheme);
                                alert.setNegativeButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                        rangeSeekBar.setEnabled(true);
                                    }
                                });
                                alert.setCancelable(false);
//                        alert.setMessage(Html.fromHtml("Time range is possible in between 00:00 - 24:00")).show();

                                rangeSeekBar.setSelectedMaxValue(24);
                            }

                            timetextmin.setText("" + rangeSeekBar.getSelectedMinValue() + ":00");
                            timetextmax.setText("" + rangeSeekBar.getSelectedMaxValue() + ":00");
                        }
                    });
                }
            }.start();

            dialog.show();
            return true;
        }
        if(id == R.id.restaurantdetails)
        {
            if(!getstringcache("currentfrag", "frag").equals("cartsm")||!getstringcache("currentfrag", "frag").equals("cartsr"))
            {
                if (getstringcache("currentfrag", "frag").equals("favoritemeals"))
                {
                    setstringcache("currentfrag","frag","cartsm");
                }
                else if (getstringcache("currentfrag", "frag").equals("restaurantdetails"))
                {
                    setstringcache("currentfrag","frag","cartsr");
                }
                else
                {
                    setstringcache("currentfrag","frag","cartsp");
                }
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                cartFragment = new CartFragment();
                fragmentTransaction.replace(R.id.con,cartFragment.newInstance(),"carat");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                setheader("Warenkorb");
                invalidateOptionsMenu();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param view
     * @param header
     */
    public void statusbasic(View view, String header) {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(1000);
        snackbar.show();
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    //checking if user has authorized app to use location or not
    public void checklocationpermission() {
        if (ContextCompat.checkSelfPermission(AllLocations.this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AllLocations.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AllLocations.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESSLOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    //handling location permission result(if user authorized or not)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESSLOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intenter = getIntent();
                    finish();
                    startActivity(intenter);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void Locationpassed(String Other,String Other1,String Otherurl,String Otherurl1, String Otherurl11, String Otherurl111) {
        if (Other.equals("locationcards"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            locationCardsFragment = new LocationCardsFragment();
            fragmentTransaction.replace(R.id.con,locationCardsFragment,"locationcards");
            fragmentTransaction.commit();
            setheader("Gastronomiebetriebe");
            setstringcache("currentfrag","frag","locationcards");
            invalidateOptionsMenu();
        }
        if (Other.equals("restaurantdetails"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            resturantDetailsFragment = new RestaurantDetailsFragment();
            fragmentTransaction.replace(R.id.con,resturantDetailsFragment.newInstance(Other1,Otherurl,Otherurl1, Otherurl11, Otherurl111),"restaurantdetails");
            setstringcache("restaurantresume","other1",Other1);
            setstringcache("restaurantresume","otherurl",Otherurl);
            setstringcache("restaurantresume","otherurl1",Otherurl1);
            setstringcache("restaurantresume","otherurl11",Otherurl11);
            setstringcache("restaurantresume","otherurl111",Otherurl111);
            fragmentTransaction.commit();
            setstringcache("currentfrag","frag","restaurantdetails");
            invalidateOptionsMenu();
        }
        if (Other.equals("payment"))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            paymentFragment = new PaymentFragment();
            fragmentTransaction.replace(R.id.con,paymentFragment.newInstance(),"payment");
            fragmentTransaction.commit();
            setheader("Bezahlen");
            setstringcache("currentfrag","frag","payment");
            invalidateOptionsMenu();
        }
    }

    public void setheader(String pros) {

        CustomTextView header = (CustomTextView) findViewById(R.id.header);
        header.setText(pros);
    }

    public void notifier() {
        getSharedPreferences("totalprice",Context.MODE_PRIVATE).edit().putFloat("count",0).apply();
        ((CartFragment) getSupportFragmentManager().findFragmentByTag("carat")).notifier();
    }

    private void logoutuser() {
        if (isOnline()) {
            class RegisterUser extends AsyncTask<String, Void, String> {
                private PostRequest ruc = new PostRequest();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in));
                    progresslayout.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String response) {
                    one.setVisibility(View.GONE);
                    two.setVisibility(View.GONE);
                    if (response.length()>0) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            String users = jsonObject.getString("error");
                            if (users.equals("OK")) {
                                loggedIn = false;
                                progressstatus.setText("Abgemeldet! Danke für\'s vorbeikommen!");
                                SharedPreferences sf = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sf.edit();
                                editor.putBoolean(Publicvars.SessionState, false);
                                editor.putString(Publicvars.KEY_EMAIL, "");
                                editor.putString(Publicvars.KEY_USERID, "");
                                editor.putString(Publicvars.KEY_TOKEN, "");
                                editor.putString(Publicvars.KEY_ROLE, "");
                                editor.apply();
                                logoutr.setVisibility(View.GONE);

                                homer.setVisibility(View.VISIBLE);
                                myordersr.setVisibility(View.VISIBLE);
                                favoritemealsr.setVisibility(View.VISIBLE);
                                howitworksr.setVisibility(View.VISIBLE);
                                userprofiler.setVisibility(View.GONE);
                                bookingsLayout.setVisibility(View.GONE);
                                SettingsLayout.setVisibility(View.GONE);
                                StatisticsLayoyt.setVisibility(View.GONE);
                                status.setVisibility(View.VISIBLE);

                                logoutFromFacebook();

                                new CartHandler(getApplicationContext()).deleteallProducts();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
                                        progresslayout.setVisibility(View.GONE);
                                        fragmentManager = getSupportFragmentManager();
                                        if(!getstringcache("currentfrag","frag").equals("locationcards"))
                                        {
                                            hometext.setTextColor(Color.parseColor("#d71a6A"));
                                            homeicon.setImageResource(R.drawable.iconhomeen);
                                            favoritemealstext.setTextColor(Color.parseColor("#333333"));
                                            favoritemealsicon.setImageResource(R.drawable.iconfavorite);
                                            howitworkstext.setTextColor(Color.parseColor("#333333"));
                                            howitworksicon.setImageResource(R.drawable.iconhowitwork);
                                            myorderstext.setTextColor(Color.parseColor("#333333"));
                                            myordersicon.setImageResource(R.drawable.iconorder);

                                            line1.setVisibility(View.VISIBLE);
                                            line2.setVisibility(View.INVISIBLE);
                                            line3.setVisibility(View.INVISIBLE);
                                            line5.setVisibility(View.INVISIBLE);

//                                            startActivity(new Intent(AllLocations.this, Login_SignUp.class));
//                                            overridePendingTransition(R.anim.right_in, R.anim.left_out);

//                                            fragmentTransaction = fragmentManager.beginTransaction();
//                                            locationCardsFragment = new LocationCardsFragment();
//                                            fragmentTransaction.replace(R.id.con,locationCardsFragment);
//                                            fragmentTransaction.commit();
//                                            setheader("Restaurants");
//                                            setstringcache("currentfrag","frag","locationcards");
                                            invalidateOptionsMenu();
                                        }
                                    }
                                }, 2000);
                            } else {
                                progressstatus.setText("Server nicht gefunden, Abmeldung nicht erfolgreich");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
                                        progresslayout.setVisibility(View.GONE);
                                    }
                                }, 2000);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        progressstatus.setText("Server nicht gefunden, Abmeldung nicht erfolgreich");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
                                progresslayout.setVisibility(View.GONE);
                            }
                        }, 2000);
                    }
                }

                @Override
                protected String doInBackground(String... params) {

                    HashMap<String, String> data = new HashMap<>();
                    data.put("session_id", getSharedPreferences(Publicvars.UserSession,Context.MODE_PRIVATE).getString(Publicvars.KEY_TOKEN,"nothing"));

                    return ruc.sendPostRequest(Publicvars.Globals + "logout", data);
                }
            }
            RegisterUser ru = new RegisterUser();
            ru.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Stellen Sie sicher, dass Sie eine aktive Internetverbindung haben", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void callactivity(String price, String quantity) {
        ((CartFragment)getSupportFragmentManager().findFragmentByTag("carat")). changeitemsinfo(price,quantity);
    }

    public  static  final int PAYPAL_REQUEST_CODE = 123;

    public void showprogress(String status) {
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);
        progresslayout.setVisibility(View.VISIBLE);
        progressstatus.setVisibility(View.VISIBLE);
        one.setVisibility(View.VISIBLE);
        two.setVisibility(View.VISIBLE);
        progressstatus.setText(status);
        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in));
        progresslayout.setVisibility(View.VISIBLE);
    }

    public void getphoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Bild auswählen"), PICK_IMAGE_REQUEST);
    }

    public void hideprogress(String status) {
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);
        progressstatus.setText(status);
        one.setVisibility(View.GONE);
        two.setVisibility(View.GONE);
        if(status.equals(""))
        {
            progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
            progresslayout.setVisibility(View.GONE);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                    progresslayout.setVisibility(View.GONE);
                }
            }, 3000);
        }
    }

    public void payer(String amount) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "EUR", "Foodsurfing Payment (android)", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent in = new Intent(AllLocations.this , PaymentActivity.class);
        in.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        in.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(in, PAYPAL_REQUEST_CODE);
    }

    public void refreshfavmeals(int position) {
        ((FavoriteMeals)getSupportFragmentManager().findFragmentByTag("favoritemeals")).changeitems(position);
    }

    public void createorder(final ArrayList<String> theids, final ArrayList<String> thequantities, final ArrayList<String> theprices, final String payid, final String type, final String quantities, final String prices, final String userids) {
        if(isOnline())
        {
            showprogress("Bestellung wird abgesendet");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals3 + "createOrder",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    if (users.equals("OK")) {
                                        hideprogress("");
                                        new CartHandler(getApplicationContext()).deleteallProducts();
                                        final Dialog dialog = new Dialog(AllLocations.this);
                                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.dialog_orderconfirm);
                                        CustomTextView close = (CustomTextView) dialog.findViewById(R.id.close);
                                        CustomTextView rno = (CustomTextView) dialog.findViewById(R.id.rno);

                                        rno.setText(jsonObject.getString("user_order"));

                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        });

                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        homer.performClick();
                                                    }
                                                });
                                            }
                                        });

                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialogInterface) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        });

                                        dialog.show();

                                    } else {
                                        hideprogress("Bestellung nicht abgesendet");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                hideprogress("Server nicht gefunden, Bestellung nicht abgesendet");
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new Hashtable<>();
                    data.put("payment_id", payid);
                    data.put("payment_source", type);
                    data.put("total_quantity", quantities);
                    data.put("total_price", prices);
                    data.put("user_id", userids);
                    for(int ac=0;ac<theids.size();ac++)
                    {
                        data.put("meals["+ac+"][id]",theids.get(ac));
                        data.put("meals["+ac+"][quantity]",thequantities.get(ac));
                        data.put("meals["+ac+"][price]",theprices.get(ac));
                    }
                    return data;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AllLocations.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            hideprogress("Bitte überprüfen Sie Ihre Internetverbindung");
        }
    }

    public void gotorestaurant(final String resid) {
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
                                        hideprogress("");
                                        if(jsonObject.getJSONArray("restaurants").length()>0)
                                        {
                                            for (int a=0;a<jsonArray.length();a++)
                                            {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                                                if(jsonObject1.getString("name").equals(resid))
                                                {
                                                    fragmentManager = getSupportFragmentManager();
                                                    fragmentTransaction = fragmentManager.beginTransaction();
                                                    resturantDetailsFragment = new RestaurantDetailsFragment();
                                                    setstringcache("restaurantresume","other1",jsonObject1.getString("name"));
                                                    setstringcache("restaurantresume","otherurl",jsonObject1.getString("img"));
                                                    setstringcache("restaurantresume","otherurl1",jsonObject1.getString("logo"));
                                                    setstringcache("restaurantresume","otherurl11",jsonObject1.getString("address") + " " + jsonObject1.getString("postal"));
                                                    setstringcache("restaurantresume","otherurl111",jsonObject1.getString("availability"));
                                                    fragmentTransaction.replace(R.id.con, resturantDetailsFragment.newInstance(jsonObject1.getString("name"), jsonObject1.getString("img"),jsonObject1.getString("logo"),jsonObject1.getString("address") + " " + jsonObject1.getString("postal"), jsonObject1.getString("availability")),"restaurantdetails");
                                                    fragmentTransaction.commit();
                                                    setheader(jsonObject1.getString("name"));

                                                    if (!getstringcache("currentfrag", "frag").equals("favoritemeals_restaurant")) {
                                                        setstringcache("currentfrag", "frag", "restaurantdetails");
                                                    }

                                                    invalidateOptionsMenu();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    hideprogress("");
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideprogress("");
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

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        new CartHandler(getApplicationContext()).deleteallProducts();

        Runtime.getRuntime().gc();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation cnfrm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(cnfrm!= null)
                {
                    try {
                        JSONObject PaDetails = cnfrm.toJSONObject();
                        JSONObject jos = PaDetails.getJSONObject("response");
                        String id = jos.getString("id");

                        final CartHandler cartHandler = new CartHandler(getApplicationContext());
                        List<GetSetProducts> locations = cartHandler.getAllProducts();

                        ArrayList<String> theids = new ArrayList<>();
                        ArrayList<String> thequantities = new ArrayList<>();
                        ArrayList<String> theprices = new ArrayList<>();

                        for (GetSetProducts cn : locations)
                        {
                            theids.add(String.valueOf(cn.getProductid()));
                            thequantities.add(String.valueOf(cartHandler.getProductsQuantityU(cn.getProductid(),cn.getProductrestaurantid())));
                            theprices.add(String.valueOf(cartHandler.getProductPrice(cn.getProductid(),cn.getProductrestaurantid())));
                        }
                        if (paymentType.toLowerCase().equals("paypal")){
                            createorder(theids,thequantities,theprices,id,"paypal",getstringcache("payer","quantity"),getstringcache("payer","price"),getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID));
                        }else {
                            createorder(theids,thequantities,theprices,donePaymentId,"Credit card",getstringcache("payer","quantity"),getstringcache("payer","price"),getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode==Activity.RESULT_CANCELED)
            {
                Log.i("paymentEx","User Cancelled");

            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                Log.i("Paymentex", "Invalid payment submitted");
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = Uri.parse(getRealPathFromURI_API19(this,data.getData()));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ((UserProfileFragment)getSupportFragmentManager().findFragmentByTag("userprofile")).saveProfileAccount(filePath);

                        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(getSupportFragmentManager().findFragmentByTag("userprofile"));
                        ft.attach(getSupportFragmentManager().findFragmentByTag("userprofile"));
                        ft.commit();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Hides virtual keyboard
     *
     * @author kvarela
     */
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public void logoutFromFacebook() {
        LoginManager.getInstance().logOut();
    }
}