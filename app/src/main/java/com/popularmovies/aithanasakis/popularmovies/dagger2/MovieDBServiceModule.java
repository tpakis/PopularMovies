package com.popularmovies.aithanasakis.popularmovies.dagger2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.popularmovies.aithanasakis.popularmovies.network.MovieDBService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 3piCerberus on 12/03/2018.
 */
//includes because it needs to know where to get okHttpClient
@Module(includes = OkHttpClientModule.class)
public class MovieDBServiceModule {
    final static String MOVIEDB_BASE_URL =
            "http://api.themoviedb.org/3/";

    @Provides
    public MovieDBService getMovieDBService(Retrofit getClient){
        return getClient.create(MovieDBService.class);
    }

    @Provides
    public Retrofit getClient(OkHttpClient okHttpClient,
                              GsonConverterFactory gsonConverterFactory, Gson gson){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MOVIEDB_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}
