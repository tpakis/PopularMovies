package com.popularmovies.aithanasakis.popularmovies.network;

import com.popularmovies.aithanasakis.popularmovies.model.MovieDBResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public interface MovieDBService {
    @GET("/3/movie/{popularOrRated}")
    Call<MovieDBResponse> getItems(@Path("popularOrRated") String popularOrRated,
                                   @Query("api_key") String apiKey);
}

