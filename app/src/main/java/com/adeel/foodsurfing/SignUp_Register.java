/**
 * <Splash>
 *
 * @author      : Muhammad Adeel
 * @owner       : Muhammad Adeel
 * @copyright   : Copyright (c) 2017 FoodSurfing
 * @created     : 22-06-2017
 * @modified    : 29-06-2017
 */
/**
 * Package of the application which was set earlier.
 */
package com.adeel.foodsurfing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SignUp_Register extends AppCompatActivity {

    EditText firstname, lastname, email, password, confirmPassword;

    /* @var Bool */
    Boolean loggedIn = false;
    /* @string user */
    String user;
    ImageButton register;
    Button login;

    CheckBox tosCheckbox;
    CustomTextView tosClick;

    RelativeLayout progresslayout;
    CustomTextView progressstatus;
    ProgressBar one,two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme3);
        setContentView(R.layout.activity_sign_up__register);

        /**
         * Creating fields.
         */
        RelativeLayout toucher = (RelativeLayout) findViewById(R.id.toucher);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);
        email = (EditText) findViewById(R.id.email);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        tosClick = (CustomTextView) findViewById(R.id.tosClick);
        tosCheckbox = (CheckBox) findViewById(R.id.tosCheckbox);
        register = (ImageButton) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);

        loggedIn = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getBoolean(Publicvars.SessionState, false);
        user = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getString(Publicvars.KEY_USERNAME, "nothing");

        if (loggedIn) {
            startActivity(new Intent(SignUp_Register.this, AllLocations.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (tosCheckbox.isChecked() && email.getText().length() > 0 && firstname.getText().length() > 0 && lastname.getText().length() > 0 && password.getText().length() > 0) {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwort bestätigen muss ausgefüllt sein und dem Feld Passwort entsprechen.", Toast.LENGTH_SHORT).show();
                }
                else {
                    registeruser(email.getText().toString(), firstname.getText().toString(), lastname.getText().toString(), password.getText().toString(), "android");
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Es gelten die Allgemeinen Geschäftsbedingungen Bitte lesen Sie die AGB und klicken Sie nachfolgend auf den Button zur Zustimmung. Beachten Sie bitte auch die Datenschutzhinweise.", Toast.LENGTH_SHORT).show();
            }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp_Register.this, Login_SignUp.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        tosClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://foodsurfing.eu/agb");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        toucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                InputMethodManager imm1 =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(firstname.getWindowToken(), 0);
                InputMethodManager imm2 =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(lastname.getWindowToken(), 0);
                InputMethodManager imm3 =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(password.getWindowToken(), 0);
            }
        });
    }


    /**
     * Volley request to register a user.
     */
    private void registeruser(final String emailer, final String firstnamer, final String lastnamer, final String passworder,final String callfromer) {
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
                            Log.i("user",users);
                            if (users.equals("OK")) {
                                progressstatus.setText("Willkommen " + emailer);
                                SharedPreferences sf = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sf.edit();
                                editor.putBoolean(Publicvars.SessionState, true);
                                editor.putString(Publicvars.KEY_EMAIL, emailer);
                                editor.putString(Publicvars.KEY_USERID, jsonObject.getString("user_id"));
                                editor.putString(Publicvars.KEY_TOKEN, jsonObject.getString("token"));
                                editor.putString(Publicvars.KEY_ROLE, "3");
                                editor.apply();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(SignUp_Register.this, AllLocations.class));
                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                        progresslayout.setVisibility(View.GONE);
                                    }
                                }, 2000);
                            } else {
                                progressstatus.setText("Email schon in Benutzung, bitte ändern");
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
                        progressstatus.setText("Server antwortet nicht");
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
                    data.put("email", params[0]);
                    data.put("firstname", params[1]);
                    data.put("lastname", params[2]);
                    data.put("password", params[3]);
                    data.put("call_from", params[4]);

                    return ruc.sendPostRequest(Publicvars.Globals + "register", data);
                }
            }
            RegisterUser ru = new RegisterUser();
            ru.execute(emailer, firstnamer, lastnamer, passworder,callfromer);
        } else {
            Toast.makeText(getApplicationContext(), "Stellen Sie sicher, dass Sie eine aktive Internetverbindung haben", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!progresslayout.isShown()) {
            super.onBackPressed();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
