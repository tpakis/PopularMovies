package com.popularmovies.aithanasakis.popularmovies.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;

import android.database.Cursor;
import android.net.Uri;
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
    private static PopularMoviesRepository MOVIESREPOSITORY;
    //retrofit

    public MovieDBService mMovieDBService;
    //room to be implemented
    //private MovieDBDAO mMovieDBDAO;
    @Inject
    public PopularMoviesRepository(MovieDBService mMovieDBService) {
       this.mMovieDBService =mMovieDBService;
        // Local db storage will be implemented
        //mMovieDBDAO = MovieDB.getDatabase().getMovieDBDAO();
    }

  /*  public synchronized static PopularMoviesRepository getInstance() {
        if (MOVIESREPOSITORY == null) {
            MOVIESREPOSITORY = new PopularMoviesRepository();
        }
        return MOVIESREPOSITORY;
    }
*/


  public Boolean isFavorite(Movie movie){
        Uri uri = Uri.withAppendedPath(MovieItem.CONTENT_URI,movie.getId().toString());
        String[] projection = {
                MovieItem.COLUMN_ID
          };
        Cursor c = MyApplication.getAppContext().getContentResolver().query(uri,projection,null,null,null);
        if (c != null) {
            return (c.getCount() > 0);
        }else {
            return false;
        }
    }
  public void deleteFavorite(Movie movie){
      Uri uri = Uri.withAppendedPath(MovieItem.CONTENT_URI,movie.getId().toString());
      MyApplication.getAppContext().getContentResolver().delete(uri,"",null);
  }
  public void storeFavorite(Movie movie){
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
      int video = movie.getVideo() ? 1 :0;
      int adult = movie.getAdult() ? 1 :0;


      ContentValues values = new ContentValues();
      values.put(MovieItem.COLUMN_ID, id);
      values.put(MovieItem.COLUMN_TITLE, title);
      values.put(MovieItem.COLUMN_OVERVIEW, overview);
      values.put(MovieItem.COLUMN_RELEASE_DATE, releaseDate);
      values.put(MovieItem.COLUMN_ORIGINAL_TITLE,originalTitle);
      values.put(MovieItem.COLUMN_ORIGINAL_LANGUAGE,originalLanguage);
      values.put(MovieItem.COLUMN_GENRES_ID,genres);
      values.put(MovieItem.COLUMN_VOTE_COUNT,voteCount);
      values.put(MovieItem.COLUMN_VOTE_AVERAGE,voteAverage);
      values.put(MovieItem.COLUMN_POPULARITY,popularity);
      values.put(MovieItem.COLUMN_POSTER_PATH,posterPath);
      values.put(MovieItem.COLUMN_BACKDROP_PATH,backdropPath);
      values.put(MovieItem.COLUMN_VIDEO,video);
      values.put(MovieItem.COLUMN_ADULT,adult);
      values.put(MovieItem.COLUMN_POSTER_BLOB,posterBlob);
      values.put(MovieItem.COLUMN_BACKDROP_BLOB,backdropBlob);
//check if exists to update...later...
      //if (mCurrentProductUri == null) {

          Uri newUri = MyApplication.getAppContext().getContentResolver().insert(MovieItem.CONTENT_URI, values);
          if (newUri == null) {
             // Toast.makeText(this, str_save_fail, Toast.LENGTH_SHORT).show();
          } else {
            //  Toast.makeText(this, str_save_success, Toast.LENGTH_SHORT).show();
          }
      /*} else {
          int rowsAffected = getContentResolver().update(mCurrentProductUri, values,
                  null, null);
          if (rowsAffected == 0) {
              Toast.makeText(this, str_update_fail, Toast.LENGTH_SHORT).show();
          } else {
              Toast.makeText(this, str_update_success, Toast.LENGTH_SHORT).show();
              finish();
          }*/
      }

    /**
     *
     * @param popularOrRated wheather to check for most popular movies or for top rated
     * @param apiKey the tmdb api key of the user
     * @return
     */
    private MutableLiveData<List<Movie>> getItemsListFromWeb(String popularOrRated, String apiKey) {
        final MutableLiveData<List<Movie>> retlist = new MutableLiveData<>();
        mMovieDBService.getItems(popularOrRated,apiKey).enqueue(new Callback<MovieDBResponse>() {
            List<Movie> items = new ArrayList<Movie>();

            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                if (response.isSuccessful()) {
                 items.addAll(response.body().getResults());
                    retlist.setValue(items);
                    // addAllToDB(itemsData);
                }
            }
            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {
                Timber.v( t.getLocalizedMessage());
            }
        });

        return retlist;
    }

    public MutableLiveData<List<MovieReviews>> getMovieReviewsFromWeb(int movieId, String apiKey) {
        final MutableLiveData<List<MovieReviews>> retlist = new MutableLiveData<>();
        mMovieDBService.getMovieReviews(movieId,apiKey).enqueue(new Callback<MovieDBReviewsResponse>() {
            List<MovieReviews> items = new ArrayList<MovieReviews>();

            @Override
            public void onResponse(Call<MovieDBReviewsResponse> call, Response<MovieDBReviewsResponse> response) {
                if (response.isSuccessful()) {
                    items.addAll(response.body().getResults());
                    retlist.setValue(items);
                }
            }
            @Override
            public void onFailure(Call<MovieDBReviewsResponse> call, Throwable t) {
                Timber.v( t.getLocalizedMessage());
            }
        });

        return retlist;
    }

    public MutableLiveData<List<MovieVideos>> getMovieVideosFromWeb(int movieId, String apiKey) {
        final MutableLiveData<List<MovieVideos>> retlist = new MutableLiveData<>();
        mMovieDBService.getMovieVideos(movieId,apiKey).enqueue(new Callback<MovieDBVideosResponse>() {
            List<MovieVideos> items = new ArrayList<MovieVideos>();

            @Override
            public void onResponse(Call<MovieDBVideosResponse> call, Response<MovieDBVideosResponse> response) {
                if (response.isSuccessful()) {
                    items.addAll(response.body().getResults());
                    retlist.setValue(items);
                }
            }
            @Override
            public void onFailure(Call<MovieDBVideosResponse> call, Throwable t) {
                Timber.v( t.getLocalizedMessage());
            }
        });

        return retlist;
    }



    /**
     * Public method to request data from the respository
     * @param query what are you looking for (popular / top rated)
     * @param apiKey the tmdb api key
     * @param internetState whether we have internet access or not
     * @return
     */
    public MutableLiveData<List<Movie>> getMoviesList(String query, String apiKey, boolean internetState) {
        //final  MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        //data.setValue(getItemsListFromWeb(query));
        if (internetState) {
            return getItemsListFromWeb(query,apiKey);
        } else {
            //it will call getListFromDB
            return getItemsListFromWeb(query,apiKey);
       //     return getItemsListFromDB("%" + query + "%");
        }
    }

}
