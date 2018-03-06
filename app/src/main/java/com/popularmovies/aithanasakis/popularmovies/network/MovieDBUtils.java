package com.popularmovies.aithanasakis.popularmovies.network;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class MovieDBUtils {
    final static String MOVIEDB_BASE_URL =
            "http://api.themoviedb.org/3/";

    public static MovieDBService getMovieDBService() {
        return RetrofitClient.getClient(MOVIEDB_BASE_URL).create(MovieDBService.class);
    }
}
