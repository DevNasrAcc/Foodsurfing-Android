package com.adeel.foodsurfing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.widget.Button;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Hashtable;

/**
 * Created by Muhammad Adeel on 12/22/2016.
 */

public class CustomButton extends android.support.v7.widget.AppCompatButton {

    /**
     * @param context
     */
    public CustomButton(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(this, context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(this, context, attrs);
    }

    /**
     * @param textview
     * @param context
     * @param attrs
     */
    public static void setCustomFont(TextView textview, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
        @SuppressLint("ResourceType") String font = a.getString(R.attr.font);
        setCustomFont(textview, font, context);
        a.recycle();
    }

    /**
     * @param textview
     * @param font
     * @param context
     */
    public static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = get(font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    /**
     * @param name
     * @param context
     * @return
     */
    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
