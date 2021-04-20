package com.adeel.foodsurfing;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import okhttp3.MultipartBody;


public class LocationTabFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String[] Header, Contact, Phone, Address;

    private GoogleMap mMap;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GPSTracker gps;

    Marker marker1;

    Location location;

    Double item_longitude, item_latitude, curr_lat, curr_long;

    public LocationTabFragment() {
        // Required empty public constructor
    }

    public static LocationTabFragment newInstance(String param1, String param2) {
        LocationTabFragment fragment = new LocationTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_location, container, false);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        final GPSTracker gpsTracker = new GPSTracker(getContext());

        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                double latitudeDiff = gpsTracker.getLatitude();
                double longitudeDiff = gpsTracker.getLongitude();

                setstringcache(Publicvars.My_Location,Publicvars.My_latitude,String.valueOf(latitudeDiff));
                setstringcache(Publicvars.My_Location,Publicvars.My_longitude,String.valueOf(longitudeDiff));

                LatLng currentUpdatedPosition = new LatLng(latitudeDiff, longitudeDiff);

                mMap.addMarker(
                        new MarkerOptions().position(currentUpdatedPosition).title("Me").icon(BitmapDescriptorFactory.fromResource(R.drawable.currentposition))
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUpdatedPosition));

                Getrestaurants(mMap);

                return false;
            }
        });

        String longitude = getstringcache(Publicvars.My_Location, Publicvars.My_longitude);
        String latitude = getstringcache(Publicvars.My_Location,Publicvars.My_latitude);

        if (!latitude.equals("clear") && !longitude.equals("clear") && !latitude.equals("null") && !longitude.equals("null")) {
            item_longitude = Double.valueOf(longitude);
            item_latitude = Double.valueOf(latitude);
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

            curr_lat = gpsTracker.getLatitude();
            curr_long = gpsTracker.getLongitude();

            LatLng currentLocation = new LatLng(curr_long, curr_lat);

            if (item_longitude != 0 && item_latitude != 0) {
                currentLocation = new LatLng(item_latitude, item_longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLocation);
                markerOptions.title("Me");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentposition));
                marker1 = mMap.addMarker(markerOptions);
            } else {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLocation);
                markerOptions.draggable(true);
                markerOptions.title("Me");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentposition));
                marker1 = mMap.addMarker(markerOptions);
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if (!marker.getTitle().equals("Me")) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_locationdetails);
                        CustomTextView itemdetailsid = (CustomTextView) dialog.findViewById(R.id.itemdetailsid);
                        CustomTextView itemdetailsheader = (CustomTextView) dialog.findViewById(R.id.itemdetailsheader);
                        CustomTextView itemdetailsaddress = (CustomTextView) dialog.findViewById(R.id.itemdetailsaddress);
                        CustomTextView itemdetailscontact = (CustomTextView) dialog.findViewById(R.id.itemdetailscontact);
                        CustomTextView itemdetailsphone = (CustomTextView) dialog.findViewById(R.id.itemdetailsphone);
                        CustomTextView apply = (CustomTextView) dialog.findViewById(R.id.apply);

                        for (int c = 0; c < 200; c++) {
                            if (getstringcache("det", "name" + c).equals(marker.getTitle())) {
                                itemdetailsid.setText(getstringcache("det", "id" + c));
                                itemdetailsheader.setText(getstringcache("det", "name" + c));
                                itemdetailsaddress.setText(getstringcache("det", "address" + c));
                                itemdetailscontact.setText(getstringcache("det", "contact" + c));
                                itemdetailsphone.setText(getstringcache("det", "phone" + c));
                                break;
                            }
                        }
                        apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CustomTextView id = (CustomTextView) dialog.findViewById(R.id.itemdetailsid);
                                setstringcache("temporary","temporaryresid", id.getText().toString());
                                ((AllLocations) getContext()).gotorestaurant(marker.getTitle());
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    } else {
                        marker.showInfoWindow();
                    }

                    return true;
                }
            });

            Getrestaurants(mMap);
        }
        else {
            Toast.makeText(getContext(), "Please choose your location", Toast.LENGTH_SHORT).show();
        }
    }

    private void Getrestaurants(final GoogleMap mMap) {
//        mMap.clear();
        if(isOnline())
        {
            gps = new GPSTracker(getContext());
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals1+"getNearbyRestaurants",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    if (users.equals("OK")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
                                        for (int a=0;a<jsonArray.length();a++)
                                        {
                                            final JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                                            setstringcache("det","id"+a,jsonObject1.getString("id"));
                                            setstringcache("det","address"+a,jsonObject1.getString("address"));
                                            setstringcache("det","name"+a,jsonObject1.getString("name"));
                                            setstringcache("det","contact"+a,jsonObject1.getString("contact_person"));
                                            setstringcache("det","phone"+a,jsonObject1.getString("phone"));
                                            LatLng markersLocation = new LatLng(Double.parseDouble(jsonObject1.get("latitude").toString()),Double.parseDouble(jsonObject1.get("longitude").toString()));
//                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markersLocation, 12.0f));
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(markersLocation);
                                            if(jsonObject1.getString("marker_icon").endsWith("redPin.png"))
                                            {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.redpin));
                                            }
                                            else
                                            {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.graypin));
                                            }
                                            markerOptions.title(jsonObject1.getString("name"));
                                            marker1 = mMap.addMarker(markerOptions);
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
                            statusbasic("Failed server connection",getView());
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
                    if(getintcache(Publicvars.My_Location,Publicvars.My_TimeMin)>=0&&getintcache(Publicvars.My_Location,Publicvars.My_TimeMax)>=0)
                    {
                        timemin = String.valueOf(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMin));
                        timemax = String.valueOf(getintcache(Publicvars.My_Location,Publicvars.My_DistanceMax));
                    }
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("longitude", longitude);
                    params.put("latitude", latitude);
                    params.put("distance", distance);
                    params.put("opening_time", timemin);
                    params.put("closing_time", timemax);
                    //returning parameters
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    public void statusbasic(String header,View view)
    {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(10000);
        snackbar.show();
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

    public boolean isOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
