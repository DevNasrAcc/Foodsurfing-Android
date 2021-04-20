package com.adeel.foodsurfing;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Observable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.FloatProperty;
import android.util.Log;

import com.adeel.foodsurfing.StripeIntegeration.ProgressDialogFragment;
import com.adeel.foodsurfing.StripeIntegeration.RetrofitFactory;
import com.adeel.foodsurfing.StripeIntegeration.StoreUtils;
import com.adeel.foodsurfing.StripeIntegeration.StripeService;
import com.jakewharton.rxbinding.view.RxView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.stripe.android.Stripe;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.model.SourceParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.wrap.pay.AndroidPayConfiguration;
import com.stripe.wrap.pay.utils.CartManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.adeel.foodsurfing.AllLocations.PAYPAL_REQUEST_CODE;
import static com.adeel.foodsurfing.AllLocations.config;
import static com.adeel.foodsurfing.AllLocations.donePaymentId;
import static com.adeel.foodsurfing.AllLocations.paymentType;

public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static float ta=0;
    CustomTextView totalitemsdata;
    public EventListener eventListener;
    CustomTextView totalamountdata;
    private List<Productflower> detailer = new ArrayList<>();
    private RecyclerView mygrid;
    private Cartitemadapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private static final int REQUEST_CODE_PAYMENT = 1;

    private CardInputWidget mCardInputWidget;
    private Stripe mStripe;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LocationCardsFragment locationCardsFragment;

    private boolean loggedIn = false;


    // Put your publishable key here. It should start with "pk_test_"
//    private static final String PUBLISHABLE_KEY = "pk_test_5jILCvHK5szaqeLoPH8Yavhq";

    // Live Stripe publishable key
    private static final String PUBLISHABLE_KEY = "pk_live_weKMGNtFS7SfMpaKZ0rGCAMF";

    public CartFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        loggedIn = getbooleancache(Publicvars.UserSession, Publicvars.SessionState);

        CustomTextView middle = (CustomTextView) view.findViewById(R.id.middle);
        CustomTextView proceed = (CustomTextView) view.findViewById(R.id.proceed);
        mygrid = (RecyclerView) view.findViewById(R.id.allmeals);
        mAdapter = new Cartitemadapter(detailer, getActivity());
        mygrid.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mygrid.setLayoutManager(mLayoutManager);
        mygrid.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mygrid.setItemAnimator(new DefaultItemAnimator());
        mygrid.setAdapter(mAdapter);
        mStripe = new Stripe(getActivity());
        totalitemsdata = (CustomTextView) view.findViewById(R.id.totalitemsdata);
        totalamountdata = (CustomTextView) view.findViewById(R.id.totalamountdata);

        final CartHandler cartHandler = new CartHandler(getContext());
        List<GetSetProducts> locations = cartHandler.getAllProducts();

        if (cartHandler.getallProductsCount()==0)
        {
            middle.setVisibility(View.VISIBLE);
        }
        else
        {
            String ti;
            ti = String.valueOf(cartHandler.getallProductsCount());
            middle.setVisibility(View.GONE);
            ta = 0;

            for (GetSetProducts cn : locations)
            {
                ta = ta + cn.getProductprice() * cn.getProductquantityu();
                Productflower fillit = new Productflower(getActivity(), String.valueOf(cn.getProductid()),cn.getProducttitle(),cn.getProductdescription(),String.valueOf(cn.getProductquantity()),String.valueOf(cn.getProductprice()),String.valueOf(cn.getProductrestaurantid()),cn.getProductstatus(),cn.getProductimage(),cn.getIs_favorite(),cn.getProductcurrency(),cn.getProductpickup(), "", "");
                detailer.add(fillit);
            }
            double roundOff = Math.round(ta * 100.0) / 100.0;
            totalamountdata.setText(""+roundOff);
            totalitemsdata.setText(ti);
        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loggedIn)
                {

                    if(cartHandler.getallProductsCount()>0) {

                        setstringcache("payer","price",totalamountdata.getText().toString());
                        setstringcache("payer","quantity",totalitemsdata.getText().toString());

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_paymentoption);
                        ImageButton closer = (ImageButton) dialog.findViewById(R.id.closer);
                        RadioGroup chooseradiogroup = (RadioGroup) dialog.findViewById(R.id.chooseradiogroup);
                        final RadioButton radio1 = (RadioButton) dialog.findViewById(R.id.radio1);
                        final RadioButton radio2 = (RadioButton) dialog.findViewById(R.id.radio2);
                        CustomTextView pay = (CustomTextView) dialog.findViewById(R.id.pay);

                        closer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(radio2.isChecked())
                                {
                                    paymentType = "paypal";
                                    setstringcache("payer","type","Credit_card");
                                    ((AllLocations)getContext()).payer(totalamountdata.getText().toString());
                                    dialog.dismiss();
                                }
                                else if(radio1.isChecked())
                                {
                                    paymentType = "credit_card";
                                    dialog.dismiss();
                                    initAndroidPay();
                                    PrompCreditCardInput();
                                }
                            }
                        });

                        dialog.show();
                    }
                    else
                    {
                        statusbasic("Stellen Sie sicher, dass Sie mindestens einen Artikel im Warenkorb haben",view);
                    }
                }
                else {
                    ((AllLocations)getContext()).showprogress("Please login to create order");
                    startActivity(new Intent(getActivity(), Login_SignUp.class));
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });

        return  view;
    }



    private void initAndroidPay() {
        AndroidPayConfiguration payConfiguration =
                AndroidPayConfiguration.init(PUBLISHABLE_KEY, "EUR");
        payConfiguration.setPhoneNumberRequired(false);
        payConfiguration.setShippingAddressRequired(true);
    }
    CompositeSubscription mCompositeSubscription;
    private ProgressDialogFragment mProgressDialogFragment;

    CustomEditText cardnum;
    CustomEditText expiryedit;
    CustomEditText cvcedit;
    private void PrompCreditCardInput(){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prompt_credit_card);

        CustomEditText cardname = (CustomEditText)dialog.findViewById(R.id.cardnameedit);
        cardnum = (CustomEditText)dialog.findViewById(R.id.cardnoedit);
        expiryedit = (CustomEditText)dialog.findViewById(R.id.expiryedit);
        cvcedit = (CustomEditText)dialog.findViewById(R.id.cvcedit);
        Button apply = (Button) dialog.findViewById(R.id.btn_purchase);
        cardnum = (CustomEditText)dialog.findViewById(R.id.cardnoedit);
        expiryedit = (CustomEditText)dialog.findViewById(R.id.expiryedit);
        cvcedit = (CustomEditText)dialog.findViewById(R.id.cvcedit);
        // CustomButton apply = (CustomButton)dialog.findViewById(R.id.cvcedit);

        cardnum.addTextChangedListener(new TextWatcher() {
            int len=0;
            private static final char space = ' ';
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = cardnum.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
            }
        });

        expiryedit.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = expiryedit.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = expiryedit.getText().toString();
                if(str.length()==2&& len <str.length()){//len check for backspace
                    expiryedit.append("/");
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(cardnum.length() == 0 || cardnum.equals("") || cardnum == null || expiryedit.length() == 0 || expiryedit.equals("") || expiryedit == null ||cvcedit.length() == 0 || cvcedit.equals("") || cvcedit == null )
                {
                    displayError("Card Field Empty");
                    return;
                }
                String[] cardData = expiryedit.getText().toString().split("/");
                String cardnums = cardnum.getText().toString().trim();
                Card card = new Card(
                        cardnums,
                        Integer.parseInt(cardData[0]),
                        Integer.parseInt(cardData[1]),
                        cvcedit.getText().toString()
                );

                if (!card.validateNumber() && !card.validateCVC()) {
                    displayError("Card Input Error");
                    return;
                }

                dismissKeyboard();
                dialog.dismiss();
                final SourceParams cardParams = SourceParams.createCardParams(card);

                rx.Observable<Source> cardSourceObservable =
                        rx.Observable.fromCallable(new Callable<Source>() {
                            @Override
                            public Source call() throws Exception {
                                return mStripe.createSourceSynchronous(
                                        cardParams,
                                        AndroidPayConfiguration.getInstance().getPublicApiKey());
                            }
                        });

                mCompositeSubscription = new CompositeSubscription();
                mProgressDialogFragment =
                        ProgressDialogFragment.newInstance(R.string.completing_purchase);

                final FragmentManager fragmentManager = getChildFragmentManager();
                mCompositeSubscription.add(cardSourceObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(
                                new Action0() {
                                    @Override
                                    public void call() {
                                        mProgressDialogFragment.show(fragmentManager, "progress");
                                    }
                                })
                        .subscribe(
                                new Action1<Source>() {
                                    @Override
                                    public void call(Source source) {
                                        proceedWithPurchaseIf3DSCheckIsNotNecessary(source);
                                    }
                                },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        if (mProgressDialogFragment != null) {
                                            mProgressDialogFragment.dismiss();
                                        }
                                        displayError(throwable.getLocalizedMessage());
                                    }
                                }));


            }
        });

        dialog.show();
    }

    private void proceedWithPurchaseIf3DSCheckIsNotNecessary(Source source) {
        Float amount = Float.parseFloat(totalamountdata.getText().toString());
        Long price = amount.longValue();
        source.setAmount(price);
        source.setCurrency("EUR");

        if (source == null || !Source.CARD.equals(source.getType())) {
            displayError("Something went wrong - this should be rare");
            return;
        }

        SourceCardData cardData = (SourceCardData) source.getSourceTypeModel();
        if (SourceCardData.REQUIRED.equals(cardData.getThreeDSecureStatus())) {
            // In this case, you would need to ask the user to verify the purchase.
            // You can see an example of how to do this in the 3DS example application.
            // In stripe-android/example.
        } else {
            // If 3DS is not required, you can charge the source.
            completePurchase(source.getId(),source);
            donePaymentId = source.getId().toString();
        }
    }


    private void completePurchase(String sourceId,Source source) {
        Retrofit retrofit = RetrofitFactory.getInstance();
        StripeService stripeService = retrofit.create(StripeService.class);
        Float amount = Float.parseFloat(totalamountdata.getText().toString());
        Long price = amount.longValue();//mCartManager.getTotalPrice();
        String numberD;
        int number;
        if(amount % 1 != 0){

            numberD = String.valueOf(amount);
            numberD = numberD.substring( numberD.indexOf ( "." ) +1);
            number = Integer.parseInt(numberD);
        }else{
            number = 0;
        }
        price *=100;
        price += number;




        if (price == null) {
            // This should be rare, and only occur if there is somehow a mix of currencies in
            // the CartManager (only possible if those are put in as LineItem objects manually).
            // If this is the case, you can put in a cart total price manually by calling
            // CartManager.setTotalPrice.
            return;
        }

        rx.Observable<Void> stripeResponse = stripeService.createQueryCharge(price, sourceId);
        final FragmentManager fragmentManager = getChildFragmentManager();
        mCompositeSubscription.add(stripeResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                if (mProgressDialogFragment != null &&
                                        !mProgressDialogFragment.isAdded())
                                    mProgressDialogFragment.show(fragmentManager, "progress");
                            }
                        })
                .doOnUnsubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                if (mProgressDialogFragment != null
                                        && mProgressDialogFragment.isVisible()) {
                                    mProgressDialogFragment.dismiss();
                                }
                            }
                        })
                .subscribe(
                        new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                finishCharge();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                displayError(throwable.getLocalizedMessage());
                            }
                        }));
    }


    private void finishCharge() {

        Float amount = Float.parseFloat(totalamountdata.getText().toString());
        Long price = amount.longValue();
        String numberD;
        int number;
        if(amount % 1 != 0){

            numberD = String.valueOf(amount);
            numberD = numberD.substring( numberD.indexOf ( "." ) +1);
            number = Integer.parseInt(numberD);
        }else{
            number = 0;
        }
        price *=100;
        price += number;


        if (price == null) {
            return;
        }
        displayPurchase(price);
    }

    private void displayPurchase(long price) {
        mProgressDialogFragment.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.purchase_complete_notification, null);

        Button donBtn = (Button)dialogView.findViewById(R.id.btn_Done);

//        TextView emojiView = (TextView) dialogView.findViewById(R.id.dlg_emoji_display);
//        // Show a smiley face!
//        emojiView.setText(StoreUtils.getEmojiByUnicode(0x1F642));
        TextView priceView = (TextView) dialogView.findViewById(R.id.dlg_price_display);
        priceView.setText(StoreUtils.getPriceString(price, null));

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        donBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CartHandler cartHandler = new CartHandler(getContext());
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
                dialog.dismiss();
                ((AllLocations)getContext()).createorder(theids,thequantities,theprices,donePaymentId,"credit_card",getstringcache("payer","quantity"),getstringcache("payer","price"),getstringcache(Publicvars.UserSession,Publicvars.KEY_USERID));
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                locationCardsFragment = new LocationCardsFragment();
                fragmentTransaction.replace(R.id.con, locationCardsFragment.newInstance(),"locationcards");
                fragmentTransaction.commit();
                setstringcache("currentfrag", "frag", "locationcards");
                ((AllLocations)getContext()).setheader("Restaurants");

                //invalidateOptionsMenu();
            }
        });

    }


    private void displayError(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void changeitemsinfo(String price,String quantity)
    {
        totalitemsdata = (CustomTextView) getView().findViewById(R.id.totalitemsdata);
        totalamountdata = (CustomTextView) getView().findViewById(R.id.totalamountdata);
        if(Float.valueOf(price)<=0) {
            totalamountdata.setText("0");
            totalitemsdata.setText("0");
        }
        else
        {
            double roundOff = Math.round(Float.valueOf(price) * 100.0) / 100.0;
            totalamountdata.setText("" + roundOff);
            totalitemsdata.setText("" + quantity);
        }
    }
    public Boolean getbooleancache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedpreferenceitemtext, false);
    }

    public void notifier()
    {
        totalitemsdata = (CustomTextView) getView().findViewById(R.id.totalitemsdata);
        totalamountdata = (CustomTextView) getView().findViewById(R.id.totalamountdata);
        CustomTextView middle = (CustomTextView) getView().findViewById(R.id.middle);
        CustomTextView proceed = (CustomTextView) getView().findViewById(R.id.proceed);
        mygrid = (RecyclerView) getView().findViewById(R.id.allmeals);
        mAdapter = new Cartitemadapter(detailer, getActivity());
        mygrid.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mygrid.setLayoutManager(mLayoutManager);
        mygrid.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mygrid.setItemAnimator(new DefaultItemAnimator());
        mygrid.setAdapter(mAdapter);

        detailer.clear();
        mAdapter.notifyDataSetChanged();

        final CartHandler cartHandler = new CartHandler(getContext());
        List<GetSetProducts> locations = cartHandler.getAllProducts();

        if (cartHandler.getallProductsCount()==0)
        {
            middle.setVisibility(View.VISIBLE);
            totalamountdata.setText("0");
            totalitemsdata.setText("0");
        }
        else
        {
            String ti;
            ti = String.valueOf(cartHandler.getallProductsCount());
            middle.setVisibility(View.GONE);
            ta = 0;

            for (GetSetProducts cn : locations)
            {
                ta = ta + cn.getProductprice() * cn.getProductquantityu();
                Productflower fillit = new Productflower(getActivity(), String.valueOf(cn.getProductid()),cn.getProducttitle(),cn.getProductdescription(),String.valueOf(cn.getProductquantity()),String.valueOf(cn.getProductprice()),String.valueOf(cn.getProductrestaurantid()),cn.getProductstatus(),cn.getProductimage(),cn.getIs_favorite(),cn.getProductcurrency(),cn.getProductpickup(), "", "");
                detailer.add(fillit);
            }
            double roundOff = Math.round(ta * 100.0) / 100.0;
            totalamountdata.setText(""+roundOff);
            totalitemsdata.setText(ti);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void statusbasic(String header,View view)
    {
        Snackbar snackbar = Snackbar
                .make(view, header, Snackbar.LENGTH_LONG)
                .setDuration(10000);
        snackbar.show();
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

    private void dismissKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
    }
}

