package com.popularmovies.aithanasakis.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;

import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBResponse;
import com.popularmovies.aithanasakis.popularmovies.network.MovieDBService;
import com.popularmovies.aithanasakis.popularmovies.network.MovieDBUtils;

import java.util.ArrayList;
import java.util.List;

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
    private MovieDBService mMovieDBService;
    //room to be implemented
    //private MovieDBDAO mMovieDBDAO;
    private PopularMoviesRepository() {
        mMovieDBService = MovieDBUtils.getMovieDBService();
        //mMovieDBDAO = MovieDB.getDatabase().getMovieDBDAO();
    }

    public synchronized static PopularMoviesRepository getInstance() {
        if (MOVIESREPOSITORY == null) {
            MOVIESREPOSITORY = new PopularMoviesRepository();
        }
        return MOVIESREPOSITORY;
    }

    public MutableLiveData<List<Movie>> getItemsListFromWeb(String popularOrRated, String apiKey) {
        final MutableLiveData<List<Movie>> retlist = new MutableLiveData<>();
        mMovieDBService.getItems(popularOrRated,apiKey).enqueue(new Callback<MovieDBResponse>() {
            List<Movie> items = new ArrayList<Movie>();
          //  Datum datum = new Datum();
     //       Link link = new Link();

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
    public MutableLiveData<List<Movie>> getMoviesList(String query, String apiKey, boolean internetState) {
        //final  MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        //data.setValue(getItemsListFromWeb(query));
        if (internetState) {
            return getItemsListFromWeb(query,apiKey);
        } else {
            return getItemsListFromWeb(query,apiKey);
       //     return getItemsListFromDB("%" + query + "%");
        }
    }

}
