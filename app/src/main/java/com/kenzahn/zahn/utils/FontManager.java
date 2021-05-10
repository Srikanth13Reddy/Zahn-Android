package com.kenzahn.zahn.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontManager {
//    public static final String ROOT = "", FONTAWESOME = ROOT + "font_icon.ttf", TEXTFONT = "OpenSans.ttf", FONTAWESOME3 = ROOT + "fontawesome.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static void setFontTypeFace(View v, Typeface typeface) {

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                setFontTypeFace(child,typeface);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        }
    }
 public static void setFontTypeFace(View v, Typeface typeface, int style) {

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                setFontTypeFace(child,typeface,style);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface,style);
        }
    }



}
