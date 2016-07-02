package com.damosdesigns.sendkisses;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by damosdesigns on 7/2/16.
 */
public class Util {

    private static SharedPreferences getSharedPrefHandle(Context context) {
        return context.getSharedPreferences(
                context.getString(R.string.shared_pref), Context.MODE_PRIVATE);
    }

    public static void writeToSharedPreferences(Context context, int prefKeyID, boolean value) {
        SharedPreferences.Editor editor = getSharedPrefHandle(context).edit();
        editor.putBoolean(context.getString(prefKeyID), value);
        editor.commit();
    }

    public static void writeToSharedPreferences(Context context, int prefKeyID, int value) {
        SharedPreferences.Editor editor = getSharedPrefHandle(context).edit();
        editor.putInt(context.getString(prefKeyID), value);
        editor.commit();
    }

    public static void writeToSharedPreferences(Context context, int prefKeyID, String value) {
        SharedPreferences.Editor editor = getSharedPrefHandle(context).edit();
        editor.putString(context.getString(prefKeyID), value);
        editor.commit();
    }

    public static boolean readSharedPref(Context context, int prefKeyID) {
        return getSharedPrefHandle(context).getBoolean(context.getString(prefKeyID), false);
    }

    public static int readSharedPref(Context context, int prefKeyID, int defaultValue) {
        return getSharedPrefHandle(context).getInt(context.getString(prefKeyID), defaultValue);
    }

    public static String readSharedPref(Context context, int prefKeyID, String defaultValue) {
        return getSharedPrefHandle(context).getString(context.getString(prefKeyID), defaultValue);
    }

}
