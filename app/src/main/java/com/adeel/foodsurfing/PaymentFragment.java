package com.adeel.foodsurfing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class PaymentFragment extends Fragment {

    View view;
    RadioGroup chooseradiogroup;
    RadioButton radio1,radio2;
    CustomEditText cardnameedit,cardnoedit,expiryedit,cvcedit;
    CustomTextView savetext;
    AppCompatCheckBox saveradio;
    Button apply;

    public PaymentFragment() {
    }

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        return fragment;
    }

    public  static  final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_payment, container, false);
        chooseradiogroup = (RadioGroup) view.findViewById(R.id.chooseradiogroup);
        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        cardnameedit = (CustomEditText) view.findViewById(R.id.cardnameedit);
        cardnoedit = (CustomEditText) view.findViewById(R.id.cardnoedit);
        expiryedit = (CustomEditText) view.findViewById(R.id.expiryedit);
        cvcedit = (CustomEditText) view.findViewById(R.id.cvcedit);
        savetext = (CustomTextView) view.findViewById(R.id.savetext);
        saveradio = (AppCompatCheckBox) view.findViewById(R.id.saveradio);
        apply = (Button) view.findViewById(R.id.apply);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(getstringcache("payer","price")),"EUR","Total",PayPalPayment.PAYMENT_INTENT_SALE);
                Intent in = new Intent(getActivity() , PaymentActivity.class);
                in.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                in.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(in,PAYPAL_REQUEST_CODE);
            }
        });

        return view;
    }
    public String getstringcache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }
}
