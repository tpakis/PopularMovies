package com.popularmovies.aithanasakis.popularmovies;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class MyApplication extends Application {
//    TODO  implement this with Dagger2
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Timber.plant(new Timber.DebugTree());
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
