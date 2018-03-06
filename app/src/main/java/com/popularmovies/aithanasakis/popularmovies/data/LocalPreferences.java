package com.popularmovies.aithanasakis.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by 3piCerberus on 06/03/2018.
 */

public class LocalPreferences {

    private static  String SORT_PARAMETER="SORT_PARAMETER";

    public static String getSortParameter(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SORT_PARAMETER,"popular");
    }

    public static void setSortParameter(String sortParameter, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SORT_PARAMETER, sortParameter);
        editor.apply();
    }



}
