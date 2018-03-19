package com.popularmovies.aithanasakis.popularmovies.repository;


import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.popularmovies.aithanasakis.popularmovies.MyApplication;
import com.popularmovies.aithanasakis.popularmovies.data.MovieContract;
import com.popularmovies.aithanasakis.popularmovies.data.MovieContract.MovieItem;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBReviewsResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBVideosResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;
import com.popularmovies.aithanasakis.popularmovies.network.MovieDBService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class PopularMoviesRepository {

    //retrofit

    public MovieDBService mMovieDBService;
    private MutableLiveData<List<Movie>> moviesListObservable = new MutableLiveData<List<Movie>>();
    private MutableLiveData<List<MovieVideos>> movieVideosObservable = new MutableLiveData<List<MovieVideos>>();
    private MutableLiveData<List<MovieReviews>> movieReviewsObservable = new MutableLiveData<List<MovieReviews>>();

    //room to be implemented
    //private MovieDBDAO mMovieDBDAO;
    @Inject
    public PopularMoviesRepository(MovieDBService mMovieDBService) {
        this.mMovieDBService = mMovieDBService;

    }


    public Boolean isFavorite(Movie movie) {
        Uri uri = Uri.withAppendedPath(MovieItem.CONTENT_URI, movie.getId().toString());
        String[] projection = {
                MovieItem.COLUMN_ID
        };
        Cursor c = MyApplication.getAppContext().getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            return (c.getCount() > 0);
        } else {
            return false;
        }
    }

    public void deleteFavorite(Movie movie) {
        Uri uri = Uri.withAppendedPath(MovieItem.CONTENT_URI, movie.getId().toString());
        MyApplication.getAppContext().getContentResolver().delete(uri, "", null);
    }

    public void storeFavorite(Movie movie) {
        int id = movie.getId();
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String releaseDate = movie.getReleaseDate();
        String originalTitle = movie.getOriginalTitle();
        String originalLanguage = movie.getOriginalLanguage();
        String genres = movie.getGenreIds().toString();
        int voteCount = movie.getVoteCount();
        double voteAverage = movie.getVoteAverage();
        double popularity = movie.getPopularity();
        String posterPath = movie.getPosterPath();
        String backdropPath = movie.getBackdropPath();
        byte[] posterBlob = movie.getPosterBlob();
        byte[] backdropBlob = movie.getPosterBlob();
        int video = movie.getVideo() ? 1 : 0;
        int adult = movie.getAdult() ? 1 : 0;


        ContentValues values = new ContentValues();
        values.put(MovieItem.COLUMN_ID, id);
        values.put(MovieItem.COLUMN_TITLE, title);
        values.put(MovieItem.COLUMN_OVERVIEW, overview);
        values.put(MovieItem.COLUMN_RELEASE_DATE, releaseDate);
        values.put(MovieItem.COLUMN_ORIGINAL_TITLE, originalTitle);
        values.put(MovieItem.COLUMN_ORIGINAL_LANGUAGE, originalLanguage);
        values.put(MovieItem.COLUMN_GENRES_ID, genres);
        values.put(MovieItem.COLUMN_VOTE_COUNT, voteCount);
        values.put(MovieItem.COLUMN_VOTE_AVERAGE, voteAverage);
        values.put(MovieItem.COLUMN_POPULARITY, popularity);
        values.put(MovieItem.COLUMN_POSTER_PATH, posterPath);
        values.put(MovieItem.COLUMN_BACKDROP_PATH, backdropPath);
        values.put(MovieItem.COLUMN_VIDEO, video);
        values.put(MovieItem.COLUMN_ADULT, adult);
        values.put(MovieItem.COLUMN_POSTER_BLOB, posterBlob);
        values.put(MovieItem.COLUMN_BACKDROP_BLOB, backdropBlob);
        //check if exists to update...later...
        //if (mCurrentProductUri == null) {

        Uri newUri = MyApplication.getAppContext().getContentResolver().insert(MovieItem.CONTENT_URI, values);
        if (newUri == null) {
            // Toast.makeText(this, str_save_fail, Toast.LENGTH_SHORT).show();
        } else {
            //  Toast.makeText(this, str_save_success, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param popularOrRated wheather to check for most popular movies or for top rated
     * @param apiKey         the tmdb api key of the user
     * @return
     */

    private void getItemsListFromWeb(String popularOrRated, String apiKey) {

        mMovieDBService.getItems(popularOrRated, apiKey).enqueue(new Callback<MovieDBResponse>() {
            List<Movie> items = new ArrayList<Movie>();

            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                if (response.isSuccessful()) {
                    items.addAll(response.body().getResults());
                    setLiveData(items);
                    // addAllToDB(itemsData);
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {
                Timber.v(t.getLocalizedMessage());
            }
        });

    }

    public void getMovieReviewsFromWeb(int movieId, String apiKey) {
        mMovieDBService.getMovieReviews(movieId, apiKey).enqueue(new Callback<MovieDBReviewsResponse>() {
            List<MovieReviews> items = new ArrayList<MovieReviews>();

            @Override
            public void onResponse(Call<MovieDBReviewsResponse> call, Response<MovieDBReviewsResponse> response) {
                if (response.isSuccessful()) {
                    items.addAll(response.body().getResults());
                    setLiveDataReviews(items);
                }
            }

            @Override
            public void onFailure(Call<MovieDBReviewsResponse> call, Throwable t) {
                Timber.v(t.getLocalizedMessage());
            }
        });

    }

    public void getMovieVideosFromWeb(int movieId, String apiKey) {
        mMovieDBService.getMovieVideos(movieId, apiKey).enqueue(new Callback<MovieDBVideosResponse>() {
            List<MovieVideos> items = new ArrayList<MovieVideos>();

            @Override
            public void onResponse(Call<MovieDBVideosResponse> call, Response<MovieDBVideosResponse> response) {
                if (response.isSuccessful()) {
                    items.addAll(response.body().getResults());
                    setLiveDataVideos(items);
                }
            }

            @Override
            public void onFailure(Call<MovieDBVideosResponse> call, Throwable t) {
                Timber.v(t.getLocalizedMessage());
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void getItemsListFromDB() {
        new AsyncTask<Void, Void, Cursor>() {
            List<Movie> items = new ArrayList<Movie>();

            @Override
            protected Cursor doInBackground(Void... params) {
                String[] projection = {
                        MovieItem.COLUMN_ID,
                        MovieItem.COLUMN_TITLE,
                        MovieItem.COLUMN_OVERVIEW,
                        MovieItem.COLUMN_VOTE_AVERAGE,
                        MovieItem.COLUMN_VOTE_COUNT,
                        MovieItem.COLUMN_POPULARITY,
                        MovieItem.COLUMN_RELEASE_DATE,
                        MovieItem.COLUMN_ADULT,
                        MovieItem.COLUMN_VIDEO,
                        MovieItem.COLUMN_GENRES_ID,
                        MovieItem.COLUMN_ORIGINAL_TITLE,
                        MovieItem.COLUMN_ORIGINAL_LANGUAGE,
                        MovieItem.COLUMN_POSTER_PATH,
                        MovieItem.COLUMN_POSTER_BLOB,
                        MovieItem.COLUMN_BACKDROP_PATH,
                        MovieItem.COLUMN_BACKDROP_BLOB
                };
                return MyApplication.getAppContext().getContentResolver().query(MovieItem.CONTENT_URI, projection, null, null, null);
            }

            @Override
            protected void onPostExecute(Cursor entries) {
                try {
                    while (entries.moveToNext()) {
                        Movie movie = new Movie(0, 0, false, 0.0, "", 0.0, "",
                                "", "", null, "", false, "", "", null, null);
                        movie.setId(entries.getInt(0));
                        movie.setTitle(entries.getString(1));
                        movie.setOverview(entries.getString(2));
                        movie.setVoteAverage(entries.getDouble(3));
                        movie.setVoteCount(entries.getInt(4));
                        movie.setPopularity(entries.getDouble(5));
                        movie.setReleaseDate(entries.getString(6));
                        movie.setAdult(entries.getInt(7) != 0);
                        movie.setVideo(entries.getInt(8) != 0);
//                        movie.setGenreIds(entries.getString(9));
                        movie.setOriginalTitle(entries.getString(10));
                        movie.setOriginalLanguage(entries.getString(11));
                        movie.setPosterPath(entries.getString(12));
                        movie.setPosterBlob(entries.getBlob(13));
                        movie.setBackdropPath(entries.getString(14));
                        movie.setBackdropBlob(entries.getBlob(15));
                        items.add(movie);
                    }
                    setLiveData(items);
                } finally {
                    entries.close();
                }
            }
        }.execute();
    }

    public MutableLiveData<List<Movie>> getLiveData() {
        return moviesListObservable;
    }

    private void setLiveData(List<Movie> entries) {
        moviesListObservable.setValue(entries);
    }

    public MutableLiveData<List<MovieVideos>> getLiveDataVideos() {
        return movieVideosObservable;
    }

    private void setLiveDataVideos(List<MovieVideos> entries) {
        movieVideosObservable.setValue(entries);
    }

    public MutableLiveData<List<MovieReviews>> getLiveDataReviews() {
        return movieReviewsObservable;
    }

    private void setLiveDataReviews(List<MovieReviews> entries) {
        movieReviewsObservable.setValue(entries);
    }

    public void getMoviesList(String query, String apiKey, boolean internetState) {
        if (query.equals("favorites")) {
            getItemsListFromDB();

        }
        if (internetState) {
            getItemsListFromWeb(query, apiKey);
        } else {
            //it will call getListFromDB
            //  return getItemsListFromWeb(query,apiKey);
            //     return getItemsListFromDB("%" + query + "%");
        }
    }
}
