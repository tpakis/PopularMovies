package com.popularmovies.aithanasakis.popularmovies.dagger2;

/**
 * Created by 3piCerberus on 12/03/2018.
 */
import android.content.Context;


import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

//includes because it needs to know where to get context
@Module(includes = AppModule.class)
public class OkHttpClientModule {

    @Provides
    public OkHttpClient okHttpClient(Cache cache){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(new StethoInterceptor())
                .build();
    }

  @Provides
    public Cache cache(File cacheFile){
        return new Cache(cacheFile, 10 * 1000 * 1000); //10 MB
    }

    @Provides
    public File file(Context context){
        File file = new File(context.getCacheDir(), "HttpCache");
        file.mkdirs();
        return file;
    }


}