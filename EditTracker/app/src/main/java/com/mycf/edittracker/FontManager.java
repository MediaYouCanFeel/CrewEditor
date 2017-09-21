package com.mycf.edittracker;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by John on 9/10/2017.
 */

public class FontManager {

    public static final String ROOT = "fonts/";
    public static final String FONTAWESOME = ROOT + "fontawesome-webfont.ttf";
    public static final String POPPINS_BOLD = ROOT + "Poppins-Bold.ttf";
    public static final String POPPINS_SEMIBOLD = ROOT + "Poppins-SemiBold.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
