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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Process;
import android.os.WorkSource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/*import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Login_SignUp extends AppCompatActivity {
    public ImageButton signinbutton,facebooklogin;
        Button signupButton, forgotPassword;

    EditText email,pass;
    LoginButton login_button;
    /* @argument CallbackManager */
    CallbackManager callbackManager;
    /* @var Bool */
    Boolean loggedIn = false;
    /* @string user */
    String user;
    CustomTextView skip;

    RelativeLayout progresslayout;
    CustomTextView progressstatus;
    ProgressBar one,two;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setTheme(R.style.AppTheme3);
        setContentView(R.layout.activity_login__sign_up);

        retrieveHashKey();

      /**
         * Creating variable of XML views like textview etc.
     */
      RelativeLayout toucher = (RelativeLayout) findViewById(R.id.toucher);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);
        email = (EditText) findViewById(R.id.emailtext);
        pass = (EditText) findViewById(R.id.emailpass);
        login_button = (LoginButton) findViewById(R.id.login_button);
        signinbutton=(ImageButton)findViewById(R.id.signinbutton);
        facebooklogin=(ImageButton)findViewById(R.id.facebooklogin);
        signupButton = (Button) findViewById(R.id.signupbutton);
        forgotPassword = (Button) findViewById(R.id.forgotpassword);
        skip = (CustomTextView) findViewById(R.id.skip);

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
                InputMethodManager imm1 =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(pass.getWindowToken(), 0);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                openProgressDialog();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                    startActivity(new Intent(Login_SignUp.this,AllLocations.class));
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    }
                }, 2000);
            }
        });

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(pass.getText().toString().length()>0) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
                    signinbutton.performClick();
                        signinbutton.setEnabled(false);
                    }
                    return true;
                }
                return false;
            }
        });

        signupButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy = new Intent(Login_SignUp.this, SignUp_Register.class);
                startActivity(toy);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });

        forgotPassword.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(Login_SignUp.this, ForgotPassword.class);
                startActivity(forgot);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length()>0&& pass.getText().length()>0)
                {
                    checkuser(email.getText().toString(),pass.getText().toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Stellen Sie sicher, dass Sie alle Felder ausgefüllt haben",Toast.LENGTH_SHORT).show();
                }
            }
        });

        facebooklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_button.performClick();
            }
        });

         /**
              * Facebook login.
           */
        callbackManager = CallbackManager.Factory.create();
        login_button.setReadPermissions(Arrays.asList("user_friends", "email", "public_profile")); //-permission  from facebook
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { //
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("adeel", loginResult.toString());
                /**
                     * App codes  .
                     */
                setFacebookData(loginResult);
                System.out.println("success");
            }

            @Override
            public void onCancel() {
                System.out.println("cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println(exception);
            }
        });
    }
        /**
            * Volley function to check if user exist in db or not, if it is, then login .
         */

    private void checkuser(final String useremail,String userpassword) {

                    if(isOnline()) {
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
                                            progressstatus.setText("Willkommen " + jsonObject.getString("name"));
                                            SharedPreferences sf = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sf.edit();
                                            editor.putBoolean(Publicvars.SessionState, true);
                                            editor.putString(Publicvars.KEY_EMAIL, useremail);
                                            editor.putString(Publicvars.KEY_USERID, jsonObject.getString("user_id"));
                                            editor.putString(Publicvars.KEY_TOKEN, jsonObject.getString("token"));
                                            editor.putString(Publicvars.KEY_ROLE, jsonObject.getString("role"));
                                            editor.apply();

                                            if (jsonObject.getString("role").equals("2")) {
                                                Log.d("adeel","Gastronomiebetriebe: " + jsonObject.getString("role"));
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(Login_SignUp.this, AllLocations.class));
                                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                                        progresslayout.setVisibility(View.GONE);
                                                    }
                                                }, 1000);
                                            }
                                            else {
                                                Log.d("adeel", "Customer: " + jsonObject.getString("role"));
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(Login_SignUp.this, AllLocations.class));
                                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                                        progresslayout.setVisibility(View.GONE);
                                                    }
                                                }, 1000);
                                            }
                                        } else {
                                            progressstatus.setText("Wrong email address or password, please try again");
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
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
                                    progressstatus.setText("Server antwortet nicht");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
                                            progresslayout.setVisibility(View.GONE);
                                        }
                                    }, 1000);
                                }
                            }

                            @Override
                            protected String doInBackground(String... params) {

                                HashMap<String, String> data = new HashMap<>();
                                data.put("email",params[0]);
                                data.put("password",params[1]);

                                return ruc.sendPostRequest(Publicvars.Globals+"login",data);
                            }
                        }
                        RegisterUser ru = new RegisterUser();
                        ru.execute(useremail,userpassword);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Stellen Sie sicher, dass Sie eine aktive Internetverbindung haben",Toast.LENGTH_SHORT).show();
                    }
    }


    /**
         * After login, with successful result, this function will get user personal info through graph api, and then call the checkuser() function to login.
         */
    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.d("adeel",response.toString());

                            String id = response.getJSONObject().getString("id");
//                            String email = response.getJSONObject().getString("email");
                            String email = (response.getJSONObject().has("email")) ? response.getJSONObject().getString("email") : response.getJSONObject().getString("id") + "@gmail.com";
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");

                            registeruser(email,firstName,lastName,"facebook",id);
                            checkuser(email, id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender"); //
        request.setParameters(parameters);
        request.executeAsync();
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

    private void registeruser(final String emailer, final String firstnamer, final String lastnamer, final String passworder, final String social_user_id) {
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
                            if (users.equals("OK") || users.equals("EMAIL_ADDRESS_EXIST")) {
                                progressstatus.setText("Willkommen " + firstnamer + " " + lastnamer);
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
                                        startActivity(new Intent(Login_SignUp.this, AllLocations.class));
                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                        progresslayout.setVisibility(View.GONE);
                                    }
                                }, 1000);
                            } else {
                                progressstatus.setText("Email schon in Benutzung, bitte ändern");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
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
                        progressstatus.setText("Server antwortet nicht");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
                                progresslayout.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                }

                @Override
                protected String doInBackground(String... params) {

                    HashMap<String, String> data = new HashMap<>();
                    data.put("email", params[0]);
                    data.put("firstname", params[1]);
                    data.put("lastname", params[2]);
                    data.put("password", params[4]);
                    data.put("call_from", "android");
                    data.put("social_user", "facebook");
                    data.put("social_user_id", params[4]);

                    return ruc.sendPostRequest(Publicvars.Globals + "register", data);
                }
            }
            RegisterUser ru = new RegisterUser();
            ru.execute(emailer, firstnamer, lastnamer, social_user_id, social_user_id);
        } else {
            Toast.makeText(getApplicationContext(), "Stellen Sie sicher, dass Sie eine aktive Internetverbindung haben", Toast.LENGTH_SHORT).show();
        }
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
            startActivity(new Intent(Login_SignUp.this,AllLocations.class));
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

    private void openProgressDialog() {
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        progressstatus = (CustomTextView) findViewById(R.id.progressstatus);
        one = (ProgressBar) findViewById(R.id.one);
        two = (ProgressBar) findViewById(R.id.two);
        progresslayout.setVisibility(View.VISIBLE);
        progressstatus.setVisibility(View.VISIBLE);
        one.setVisibility(View.VISIBLE);
        two.setVisibility(View.VISIBLE);
        progressstatus.setText("Überspringen - Einen Moment bitte!");
        progresslayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in));
        progresslayout.setVisibility(View.VISIBLE);
    }

    /**
     * Retreiving Hash Key
     */
    private void retrieveHashKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.adeel.foodsurfing", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
