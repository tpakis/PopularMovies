package com.popularmovies.aithanasakis.popularmovies.network;

import com.popularmovies.aithanasakis.popularmovies.model.MovieDBResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBReviewsResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBVideosResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public interface MovieDBService {
    @GET("movie/{popularOrRated}")
    Call<MovieDBResponse> getItems(@Path("popularOrRated") String popularOrRated,
                                   @Query("api_key") String apiKey);


    @GET("movie/{id}/videos")
    Call<MovieDBVideosResponse> getMovieVideos(@Path("id") int movieId,
                                                        @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieDBReviewsResponse> getMovieReviews(@Path("id") int movieId,
                                                 @Query("api_key") String apiKey);
}

