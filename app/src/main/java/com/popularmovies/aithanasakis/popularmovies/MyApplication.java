package com.popularmovies.aithanasakis.popularmovies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.popularmovies.aithanasakis.popularmovies.dagger2.AppModule;
import com.popularmovies.aithanasakis.popularmovies.dagger2.DaggerMainActivityViewModelComponent;
import com.popularmovies.aithanasakis.popularmovies.dagger2.MainActivityViewModelComponent;
import com.popularmovies.aithanasakis.popularmovies.dagger2.PopularMoviesRepositoryModule;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.MainActivityViewModel;

import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class MyApplication extends Application {


    //    TODO  implement this with Dagger2
    public MainActivityViewModelComponent mainActivityViewModelComponent;
    private static Context context;



    private static MyApplication myApplication;

    public void onCreate() {
        super.onCreate();
        myApplication=this;
        MyApplication.context = getApplicationContext();
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
            initDaggerComponent();
            initializeStetho();

        }
    }
    public static MyApplication getMyApplication() {
        return myApplication;
    }

    private void initDaggerComponent(){

        mainActivityViewModelComponent = DaggerMainActivityViewModelComponent
                .builder()
                .popularMoviesRepositoryModule(new PopularMoviesRepositoryModule())
                .appModule(new AppModule(this))
                .build();
    }

    public MainActivityViewModelComponent getMainActivityViewModelComponent() {
        return mainActivityViewModelComponent;
    }

    public void  initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
