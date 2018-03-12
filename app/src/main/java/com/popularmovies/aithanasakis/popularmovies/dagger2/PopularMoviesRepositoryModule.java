package com.popularmovies.aithanasakis.popularmovies.dagger2;

import com.popularmovies.aithanasakis.popularmovies.network.MovieDBService;
import com.popularmovies.aithanasakis.popularmovies.repository.PopularMoviesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 3piCerberus on 12/03/2018.
 */
@Module(includes = MovieDBServiceModule.class)
public class PopularMoviesRepositoryModule {

    @Provides
    @Singleton
    public PopularMoviesRepository getPopoularMoviesRepository(MovieDBService movieDBService){
        return new PopularMoviesRepository(movieDBService);
    }
}
