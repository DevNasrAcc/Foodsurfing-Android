package com.adeel.foodsurfing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class ChooseLocationFragment extends Fragment {
    public EventListener eventListener;

    GPSTracker gpsTracker;

    public ChooseLocationFragment() {
        // Required empty public constructor
    }
    public static ChooseLocationFragment newInstance(String param1, String param2) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_location, container,false);
        final RelativeLayout rester = (RelativeLayout) view.findViewById(R.id.rester);
        //user have not set the location
        final RelativeLayout first = (RelativeLayout) view.findViewById(R.id.first);
        CustomTextView yourcitytext = (CustomTextView) first.findViewById(R.id.yourcitytext);
        final EditText yourcitydata = (EditText) first.findViewById(R.id.yourcitydata);
        CustomTextView postboxtext = (CustomTextView) first.findViewById(R.id.postboxtext);
        final EditText postboxdata = (EditText) first.findViewById(R.id.postboxdata);
        rester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yourcitydata.getWindowToken(), 0);
                InputMethodManager imm1 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(postboxdata.getWindowToken(), 0);
            }
        });


        RelativeLayout curloclayout = (RelativeLayout) first.findViewById(R.id.curloclayout);
        RelativeLayout othloclayout = (RelativeLayout) first.findViewById(R.id.othloclayout);

        othloclayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AllLocations)getContext()).showprogress("Ihr Standort wird gesucht...");
                if(yourcitydata.getText().length()>0&&postboxdata.getText().length()>0)
                {
                    Geocoder gc = new Geocoder(getContext());
                    if(gc.isPresent()){
                        List<Address> list = null;
                        try {
                            list = gc.getFromLocationName(yourcitydata.getText().toString(), 1);
                            if(list.size()>0)
                            {
                                Address address = list.get(0);
                                double lat = address.getLatitude();
                                double lng = address.getLongitude();
                                setstringcache(Publicvars.My_Location,Publicvars.My_Area,yourcitydata.getText().toString());
                                setstringcache(Publicvars.My_Location,Publicvars.My_Postcode,postboxdata.getText().toString());
                                setintcache(Publicvars.My_Location,Publicvars.My_DistanceMax,100);
                                setintcache(Publicvars.My_Location,Publicvars.My_DistanceMin,0);
                                setstringcache(Publicvars.My_Location,Publicvars.My_latitude,String.valueOf(lat));
                                setstringcache(Publicvars.My_Location,Publicvars.My_longitude,String.valueOf(lng));
                                eventListener.Locationpassed("locationcards","","","","","");
                                ((AllLocations)getContext()).hideprogress("Gastronomiebetriebe werden gesucht….");
                                setbooleancache(Publicvars.My_Location,Publicvars.My_Check,false);
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Stellen Sie sicher, dass Sie die Stadt korrekt eingegeben haben");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        ((AllLocations)getContext()).hideprogress("Bitte GPS einschalten");
                    }
                }
                else
                {
                    ((AllLocations)getContext()).hideprogress("Stellen Sie sicher, dass Sie die Stadt korrekt eingegeben haben");
                }
            }
        });

        curloclayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // geo location ka code hga is k ander se if ki condition lgege jisme fields dekhnge empty tou nhi hy ..field are area and postalcode agr une khali nhi chora hga tou area se longitude latitude nklange</code>

                gpsTracker = new GPSTracker(getContext());

                if (!gpsTracker.isGPSEnabled())
                {
                    gpsTracker.showSettingsAlert();
                }

                ((AllLocations)getContext()).showprogress("Ihr Standort wird gesucht...");
                if(gpsTracker.getLatitude()>0&&gpsTracker.getLongitude()>0)
                {
                    double lat = gpsTracker.getLatitude();
                    double lng = gpsTracker.getLongitude();

                    setstringcache(Publicvars.My_Location,Publicvars.My_Area,"clear");
                    setstringcache(Publicvars.My_Location,Publicvars.My_Postcode,"clear");
                    setintcache(Publicvars.My_Location,Publicvars.My_DistanceMax,100);
                    setintcache(Publicvars.My_Location,Publicvars.My_DistanceMin,0);
                    setstringcache(Publicvars.My_Location,Publicvars.My_latitude,String.valueOf(lat));
                    setstringcache(Publicvars.My_Location,Publicvars.My_longitude,String.valueOf(lng));
                    eventListener.Locationpassed("locationcards","","","","","");
                    ((AllLocations)getContext()).hideprogress("Gastronomiebetriebe werden gesucht….");
                    setbooleancache(Publicvars.My_Location,Publicvars.My_Check,true);
                }
                else
                {
                    ((AllLocations)getContext()).hideprogress("Something went wrong with the GPS settings, please turn on Your GPS manually");
                }
            }
        });

        final RelativeLayout second = (RelativeLayout) view.findViewById(R.id.second);
        CustomTextView nolocfirst = (CustomTextView) second.findViewById(R.id.nolocfirst);
        CustomTextView nolocsecond = (CustomTextView) second.findViewById(R.id.nolocsecond);
        final CustomTextView nolocthird = (CustomTextView) second.findViewById(R.id.nolocthird);
        CustomTextView nolocfourth = (CustomTextView) second.findViewById(R.id.nolocfourth);
// intent k through browser ma link kholdia
        nolocthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+nolocthird.getText().toString()));
                startActivity(browserIntent);
            }
        });
// agr user dubara try krna chahe tou first wali visible hojae
        nolocfourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                second.setVisibility(View.INVISIBLE);
                first.setVisibility(View.VISIBLE);
            }
        });

        return view;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EventListener) {
            eventListener = (EventListener)context;
        } else {
            // Throw an error!
        }
    }
}
