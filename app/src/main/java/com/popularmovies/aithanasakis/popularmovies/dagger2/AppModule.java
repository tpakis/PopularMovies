package com.popularmovies.aithanasakis.popularmovies.dagger2;

import android.content.Context;

import com.popularmovies.aithanasakis.popularmovies.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 3piCerberus on 12/03/2018.
 */
@Module
public class AppModule {

    MyApplication mApplication;

    public AppModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    public Context getAppContext(){
       return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    MyApplication providesApplication() {
        return mApplication;
    }

}