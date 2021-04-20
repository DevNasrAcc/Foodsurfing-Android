package com.adeel.foodsurfing;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;

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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;

public class UserProfileFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RelativeLayout morelayout;
    RoundedImageView dpimage;
    CustomTextView dptext,fnametext,lnametext,emailtext,passwordtext,cpasswordtext,moretext,streettext,regiontext,citytext,countrytext,postaltext;
    CustomEditText fnameedit,lnameedit,emailedit,passwordedit,cpasswordedit,streetedit,regionedit,cityedit,countryedit,postaledit;
    Button update;

    String Role;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public static String fina="";
    public static String lana="";
    public static String ema="";
    public static String dpim="";
    public static String stna="";
    public static String rena="";
    public static String cina="";
    public static String cona="";
    public static String poco="";
    View view;
    private String mParam1;
    private String mParam2;
    private int PICK_IMAGE_REQUEST = 1;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_user_profile, container,false);

        RelativeLayout toucher = (RelativeLayout) view.findViewById(R.id.toucher);
        ((AllLocations)getContext()).showprogress("Ihre Daten werden gesucht...");
        dpimage = (RoundedImageView) view.findViewById(R.id.dpimage);
        dptext = (CustomTextView) view.findViewById(R.id.dptext);
        fnametext = (CustomTextView) view.findViewById(R.id.fnametext);
        lnametext = (CustomTextView) view.findViewById(R.id.lnametext);
        emailtext = (CustomTextView) view.findViewById(R.id.emailtext);
        passwordtext = (CustomTextView) view.findViewById(R.id.passwordtext);
        cpasswordtext = (CustomTextView) view.findViewById(R.id.cpasswordtext);
        fnameedit = (CustomEditText) view.findViewById(R.id.fnameedit);
        lnameedit = (CustomEditText) view.findViewById(R.id.lnameedit);
        emailedit = (CustomEditText) view.findViewById(R.id.emailedit);
        passwordedit = (CustomEditText) view.findViewById(R.id.passwordedit);
        cpasswordedit = (CustomEditText) view.findViewById(R.id.cpasswordedit);;
        update = (Button) view.findViewById(R.id.update);

        moretext = (CustomTextView) view.findViewById(R.id.moretext);
        morelayout = (RelativeLayout) view.findViewById(R.id.morelayout);
        streettext = (CustomTextView) view.findViewById(R.id.streettext);
        streetedit = (CustomEditText) view.findViewById(R.id.streetedit);
        postaltext = (CustomTextView) view.findViewById(R.id.postaltext);
        postaledit = (CustomEditText) view.findViewById(R.id.postaledit);
        regiontext = (CustomTextView) view.findViewById(R.id.regiontext);
        regionedit = (CustomEditText) view.findViewById(R.id.regionedit);
        citytext = (CustomTextView) view.findViewById(R.id.citytext);
        cityedit = (CustomEditText) view.findViewById(R.id.cityedit);
        countrytext = (CustomTextView) view.findViewById(R.id.countrytext);
        countryedit = (CustomEditText) view.findViewById(R.id.countryedit);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Publicvars.UserSession,MODE_PRIVATE);
        Role = sharedPreferences.getString(Publicvars.KEY_ROLE,"0");
        if (Role.equals("2")) {
            morelayout.setVisibility(View.GONE);
            moretext.setVisibility(View.GONE);
            fnameedit.setEnabled(false);
            lnameedit.setEnabled(false);
            emailedit.setEnabled(false);

            fnametext.setText("Name des Betriebs");
            lnametext.setText("Ansprechpartner/in");
        }

        moretext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (morelayout.isShown())
                {
                    morelayout.setVisibility(View.GONE);
                    moretext.setText(R.string.more_details);
                }
                else
                {
                    morelayout.setVisibility(View.VISIBLE);
                    moretext.setText(R.string.less_details);
                }
            }
        });

        Getuserinfo();
        dptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordedit.getText().toString().length()>0&&cpasswordedit.getText().toString().length()>0&&
                        passwordedit.getText().toString().equals(cpasswordedit.getText().toString()))
                {
                    if(!fnameedit.getText().toString().equals(fina)||
                            !lnameedit.getText().toString().equals(lana)||
                            !streetedit.getText().toString().equals(stna)||
                            !postaledit.getText().toString().equals(poco)||
                            !regionedit.getText().toString().equals(rena)||
                            !cityedit.getText().toString().equals(cina)||
                            !countryedit.getText().toString().equals(cona)||
                            !emailedit.getText().toString().equals(ema))
                    {
                        ((AllLocations)getContext()).showprogress("Ihre Daten werden aktualisiert...");
                        Updateuser(fnameedit.getText().toString(),lnameedit.getText().toString(),emailedit.getText().toString(),passwordedit.getText().toString(),cpasswordedit.getText().toString(),streetedit.getText().toString(),postaledit.getText().toString(),cityedit.getText().toString(),regionedit.getText().toString(),countryedit.getText().toString());
                    }
                    else if(fnameedit.getText().toString().equals(fina)||
                            lnameedit.getText().toString().equals(lana)||
                            emailedit.getText().toString().equals(ema)&&
                                    Role.equals("2"))
                    {
                        ((AllLocations)getContext()).showprogress("Ihre Daten werden aktualisiert...");
                        Updateuser(fnameedit.getText().toString(),lnameedit.getText().toString(),emailedit.getText().toString(),passwordedit.getText().toString(),cpasswordedit.getText().toString(),"no","no","no","no","no");
                    }
                }
                else
                {
                    if(!fnameedit.getText().toString().equals(fina)||
                            !lnameedit.getText().toString().equals(lana)||
                            !streetedit.getText().toString().equals(stna)||
                            !postaledit.getText().toString().equals(poco)||
                            !regionedit.getText().toString().equals(rena)||
                            !cityedit.getText().toString().equals(cina)||
                            !countryedit.getText().toString().equals(cona)||
                            !emailedit.getText().toString().equals(ema))
                    {
                        ((AllLocations)getContext()).showprogress("Ihre Daten werden aktualisiert...");
                        Updateuser(fnameedit.getText().toString(),lnameedit.getText().toString(),emailedit.getText().toString(),"no","no",streetedit.getText().toString(),postaledit.getText().toString(),cityedit.getText().toString(),regionedit.getText().toString(),countryedit.getText().toString());
                    }
                }
            }
        });

        toucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(lnameedit.getWindowToken(), 0);
                InputMethodManager imm1 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(streetedit.getWindowToken(), 0);
                InputMethodManager imm2 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(postaledit.getWindowToken(), 0);
                InputMethodManager imm3 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(regionedit.getWindowToken(), 0);
                InputMethodManager imm4 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(cityedit.getWindowToken(), 0);
                InputMethodManager imm5 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(countryedit.getWindowToken(), 0);
                InputMethodManager imm6 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm6.hideSoftInputFromWindow(emailedit.getWindowToken(), 0);
                InputMethodManager imm7 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm7.hideSoftInputFromWindow(fnameedit.getWindowToken(), 0);
                InputMethodManager imm8 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm8.hideSoftInputFromWindow(passwordedit.getWindowToken(), 0);
                InputMethodManager imm9 =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm9.hideSoftInputFromWindow(cpasswordedit.getWindowToken(), 0);
            }
        });

        return view;
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);

            ((AllLocations)getContext()).getphoto();
        } else {
            ((AllLocations)getContext()).getphoto();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ((AllLocations)getContext()).getphoto();
        }else {
            ((AllLocations)getContext()).showprogress("Permission required");
        }
    }

    private void Updateuser(final String firstnamer, final String lastnamer, final String emailr, final String passwordr, final String confirmpasswordr, final String streetr, final String postalr, final String cityr, final String regionr, final String countryr) {
        final String userid = getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID);
        if(isOnline())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals2+"updateProfile",
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
                                        ((AllLocations)getContext()).hideprogress("Aktualisierung erfolgreich");
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Ihre Daten werden leider nicht aktualisiert");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Ihre Daten werden leider nicht aktualisiert");
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((AllLocations)getContext()).hideprogress("No Connection with server");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("user_id", userid);
                    params.put("firstname", firstnamer);
                    params.put("lastname", lastnamer);
                    params.put("email", emailr);
                    if(!passwordr.equals("no")&&!confirmpasswordr.equals("no"))
                    {
                        params.put("password", passwordr);
                        params.put("confirm_password", confirmpasswordr);
                    }
                    if(!streetr.equals(""))
                    {
                        params.put("street_name", streetr);
                    }
                    if(!postalr.equals(""))
                    {
                        params.put("postal", postalr);
                    }
                    if(!regionr.equals(""))
                    {
                        params.put("region", regionr);
                    }
                    if(!cityr.equals(""))
                    {
                        params.put("city", cityr);
                    }
                    if(!countryr.equals(""))
                    {
                        params.put("country", countryr);
                    }
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

    private void Getuserinfo() {
        ((AllLocations)getContext()).showprogress("Ihre Daten werden gesucht...");
        final String userid = getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID);
        if(isOnline())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Publicvars.Globals2+"get",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length()>0) {
                                JSONObject jsonObject;

                                try {
                                    jsonObject = new JSONObject(response);
                                    String users = jsonObject.getString("error");
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                                    if (users.equals("OK")) {
                                        for (int a=0;a<jsonObject1.length();a++)
                                        {
                                            fina = jsonObject1.getString("firstname");
                                            lana = jsonObject1.getString("lastname");
                                            ema = jsonObject1.getString("email");
                                            dpim = jsonObject1.getString("user_image");
                                            poco = jsonObject1.getString("postal");
                                            stna = jsonObject1.getString("street_name");
                                            cona = jsonObject1.getString("country");
                                            cina = jsonObject1.getString("city");
                                            rena = jsonObject1.getString("region");
                                        }

                                        fnameedit.setText(fina);
                                        lnameedit.setText(lana);
                                        emailedit.setText(ema);
                                        ((AllLocations)getContext()).hideprogress("Ihre Daten nicht gefunden");
                                        Glide.with(getContext()).load(dpim)
                                                .thumbnail(0.5f)
                                                .crossFade()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(dpimage);
                                        ((AllLocations)getContext()).hideprogress("");
                                        if(!stna.equals("null")&&!stna.equals(""))
                                        {
                                            streetedit.setText(stna);
                                        }
                                        if(!poco.equals("null")&&!poco.equals(""))
                                        {
                                            postaledit.setText(poco);
                                        }
                                        if(!cona.equals("null")&&!cona.equals(""))
                                        {
                                            countryedit.setText(cona);
                                        }
                                        if(!cina.equals("null")&&!cina.equals(""))
                                        {
                                            cityedit.setText(cina);
                                        }
                                        if(!rena.equals("null")&&!rena.equals(""))
                                        {
                                            regionedit.setText(rena);
                                        }
                                    }
                                    else
                                    {
                                        ((AllLocations)getContext()).hideprogress("Ihre Daten nicht gefunden");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ((AllLocations)getContext()).hideprogress("Something went wrong");
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((AllLocations)getContext()).hideprogress("No Connection with server");
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

    public void setstringcache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }

    public void setintcache(String sharedpreferencename, String sharedpreferenceitemtext, int sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public int getintcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getInt(sharedpreferenceitemtext, 0);
    }

    public void setbooleancache(String sharedpreferencename, String sharedpreferenceitemtext, Boolean sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void saveProfileAccount(final Uri bits) throws Exception {
        try {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
            File imageFile = new File(getRealPathFromURI(bits));

            final String userid = getstringcache(Publicvars.UserSession, Publicvars.KEY_USERID);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userid)
                    .addFormDataPart("source", "android")
                    .addFormDataPart("file", imageFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageFile))
                    .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(Publicvars.Globals2 + "profilePicture")
                    .post(requestBody)
                    .build();

            final OkHttpClient client = new OkHttpClient();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);
                Log.i("adeel", response.body().toString());
            } catch (Exception e) {
                Log.e("adeel", "Other Error: " + e.getLocalizedMessage());
            }
        }
        catch (Exception e) {
            Log.e("adeel", "Other Error: " + e.getLocalizedMessage());
        }
    }
}
