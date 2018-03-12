package com.popularmovies.aithanasakis.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.popularmovies.aithanasakis.popularmovies.BuildConfig;
import com.popularmovies.aithanasakis.popularmovies.MyApplication;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.repository.PopularMoviesRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class MainActivityViewModel extends ViewModel {
    private static boolean internetState = false;
    private String theMovieDBBApiKey;

    @Inject
    public PopularMoviesRepository popularRepository;
    private MediatorLiveData<List<Movie>> itemsListObservable;

    public MainActivityViewModel () {
        super();
        theMovieDBBApiKey = BuildConfig.THEMOVIEDB_API_KEY;
        itemsListObservable = new MediatorLiveData<>();
        MyApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
     //   popularRepository = PopularMoviesRepository.getInstance();

    }

    public LiveData<List<Movie>> getMoviesItemsList(String query) {
        itemsListObservable.addSource(
                popularRepository.getMoviesList(query, theMovieDBBApiKey, internetState),
                apiResponse -> itemsListObservable.setValue(apiResponse)
        );
        return itemsListObservable;
        //    itemsListObservable = nasaRepository.getMyNasaItemsList(query,internetState);
    }

    public void setInternetState(boolean internetState) {
        this.internetState = internetState;
        Timber.v(String.valueOf(internetState));
    }

    public LiveData<List<Movie>> getItemsListObservable() {
        return itemsListObservable;
    }

}
