package com.popularmovies.aithanasakis.popularmovies.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class RetrofitClient {
    private static Retrofit INSTANSE = null;

    public static Retrofit getClient(String baseUrl) {
        if (INSTANSE == null) {
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            INSTANSE = new Retrofit.Builder()
                    .client(okClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANSE;
    }
}
