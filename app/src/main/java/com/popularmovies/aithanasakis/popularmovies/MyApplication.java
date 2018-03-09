package com.popularmovies.aithanasakis.popularmovies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

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
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
            initializeStetho();
        }
    }
    public void  initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
