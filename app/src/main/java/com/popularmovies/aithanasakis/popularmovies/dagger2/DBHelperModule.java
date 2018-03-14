package com.popularmovies.aithanasakis.popularmovies.dagger2;
import android.content.Context;

import com.popularmovies.aithanasakis.popularmovies.data.DbHelper;

import dagger.Module;
import dagger.Provides;
/**
 * Created by 3piCerberus on 14/03/2018.
 */
@Module(includes = AppModule.class)
public class DBHelperModule {
    
    @Provides
    public DbHelper movieDBHelper(Context context){
        return new DbHelper(context);
    }




}
