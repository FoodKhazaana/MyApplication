package com.bhojnalya.vikas.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by RaviAgrawal on 02-09-2017.
 */

public class SharedPrefHelper {
    public static final String PREFS_NAME = "FoodKhazanaPref";
    public static SharedPreferences mPref;
    public static SharedPreferences.Editor edit;
    public static final String CONTACT_KEY="contact";
    public static Context act;

    public static void initializePreferences(Context paramContext)
    {
        mPref = paramContext.getSharedPreferences(PREFS_NAME, act.MODE_PRIVATE);
        edit = mPref.edit();
        act = paramContext;
    }

    public static String getObjectAsString(String key)
    {
        return mPref.getString(key,null);
    }

    public static void putObjectAsString(String key,String value)
    {
        edit.putString(key,value);
        edit.commit();
    }
}
