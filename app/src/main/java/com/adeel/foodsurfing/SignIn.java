/**
 * <SignIn>
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    Boolean loggedIn = false;
    TextView textView;
    Button logout;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /**
         * Creating variables for xml views.
         */
        logout = (Button) findViewById(R.id.logout);
        textView = (TextView) findViewById(R.id.logger);
        loggedIn = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getBoolean(Publicvars.SessionState,false);
        user = getSharedPreferences(Publicvars.UserSession, Context.MODE_PRIVATE).getString(Publicvars.KEY_USERNAME,"nothing");
        if(loggedIn)
        {
            textView.setText(user);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not logged In redirecting to login",Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignIn.this,Login_SignUp.class));
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SignIn.this)
                        .setTitle("Abmelden")
                        .setMessage("Sind Sie sicher?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getSharedPreferences(Publicvars.UserSession,Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Publicvars.KEY_USERNAME,"username");
                                editor.putString(Publicvars.KEY_EMAIL,"email");
                                editor.putBoolean(Publicvars.SessionState,false);
                                editor.apply();
                                dialog.cancel();
                                startActivity(new Intent(SignIn.this,Login_SignUp.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


    }
}
