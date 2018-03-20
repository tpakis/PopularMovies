package com.popularmovies.aithanasakis.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.popularmovies.aithanasakis.popularmovies.R;

/**
 * Created by 3piCerberus on 06/03/2018.
 */

public class LocalPreferences {

    private static  String SORT_PARAMETER="SORT_PARAMETER";

    public static int getSortParameter(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(SORT_PARAMETER, R.id.sort_by_popularity);
    }

    public static void setSortParameter(int sortParameter, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(SORT_PARAMETER, sortParameter);
        editor.apply();
    }



}
