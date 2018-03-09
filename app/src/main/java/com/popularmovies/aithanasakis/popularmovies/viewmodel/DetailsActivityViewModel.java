package com.popularmovies.aithanasakis.popularmovies.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.popularmovies.aithanasakis.popularmovies.model.Movie;

import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class DetailsActivityViewModel extends ViewModel{
    private static boolean internetState = false;
    private static Movie selectedMovie;

    public DetailsActivityViewModel() {
        selectedMovie = new Movie(0,0,false,0.0,"",0.0,"",
                "","",null,"",false,"","");
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }



    public void setInternetState(boolean internetState) {
        this.internetState = internetState;
        Timber.v(String.valueOf(internetState));
    }
}
