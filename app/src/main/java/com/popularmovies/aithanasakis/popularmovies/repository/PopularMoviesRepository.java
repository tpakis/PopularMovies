package com.popularmovies.aithanasakis.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;

import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBResponse;
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
