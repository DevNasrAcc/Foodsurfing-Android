/**
 * <Login_SignUp>
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
    ImageButton forgotButton;
    Button signupButton, signinButton;

    EditText email;
    /* @argument CallbackManager */
    CallbackManager callbackManager;

    /* @var Bool */
    Boolean loggedIn = false;

    /* @string user */
    String user;

    RelativeLayout progresslayout;
    CustomTextView progressstatus;
    ProgressBar one,two;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setTheme(R.style.AppTheme3);
        setContentView(R.layout.activity_forgot_password);

      /**
         * Creating variable of XML views like textview etc.
     */
      RelativeLayout toucher = (RelativeLayout) findViewById(R.id.toucher);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);
        email = (EditText) findViewById(R.id.forgotemailtext);
        forgotButton=(ImageButton)findViewById(R.id.forgotButton);
        signinButton=(Button)findViewById(R.id.signinButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        /*
         * Checking if user is logged in or not, through a variable which is saved in sharedprefrences .
         */
        loggedIn = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getBoolean(Publicvars.SessionState,false);
        user = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getString(Publicvars.KEY_USERNAME,"nothing");

        toucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
            }
        });

        signupButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_user_screen = new Intent(ForgotPassword.this, SignUp_Register.class);
                startActivity(signup_user_screen);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signin_user_screen = new Intent(ForgotPassword.this, Login_SignUp.class);
                startActivity(signin_user_screen);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length() > 0)
                {
                    forgotPassword(email.getText().toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Stellen Sie sicher, dass Sie alle Felder ausgefüllt haben",Toast.LENGTH_SHORT).show();
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();
    }

    /**
         * Checking if the device has an active internet connection or not.
         */

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
        else {
            startActivity(new Intent(ForgotPassword.this,AllLocations.class));
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Für Zurück bitte nochmal klicken!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /**
     * Check email address and ask for resetting it
     *
     * @param userEmail
     */
    @SuppressWarnings("JavaDoc")
    private void forgotPassword(final String userEmail) {
        if(isOnline()) {
            class ForgotUserPassword extends AsyncTask<String, Void, String> {
                private PostRequest request = new PostRequest();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
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
                            String result = jsonObject.getString("error");

                            if (result.equals("OK")) {
                                Toast.makeText(getApplicationContext(), "The new password has been sent to the given email address. Please login with new credentials", Toast.LENGTH_SHORT).show();

                                SharedPreferences sf = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sf.edit();
                                editor.putBoolean(Publicvars.SessionState, false);
                                editor.putString(Publicvars.KEY_EMAIL, userEmail);
                                editor.apply();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(ForgotPassword.this, Login_SignUp.class));
                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                        progresslayout.setVisibility(View.GONE);
                                    }
                                }, 1000);
                            } else {
                                progressstatus.setText("Bitte geben Sie die korrekte E-mail Adresse ein");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                        progresslayout.setVisibility(View.GONE);

                                    }
                                }, 1000);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        progressstatus.setText("Server is not responding");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                progresslayout.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                }

                @Override
                protected String doInBackground(String... params) {

                    HashMap<String, String> data = new HashMap<>();
                    data.put("email", params[0]);

                    return request.sendPostRequest(Publicvars.Globals + "forgetPassword", data);
                }
            }

            ForgotUserPassword forgotPassword = new ForgotUserPassword();
            forgotPassword.execute(userEmail);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Stellen Sie sicher, dass Sie eine aktive Internetverbindung haben", Toast.LENGTH_SHORT).show();
        }
    }
}
