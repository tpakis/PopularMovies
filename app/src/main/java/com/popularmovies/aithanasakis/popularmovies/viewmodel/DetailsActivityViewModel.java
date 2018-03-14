package com.popularmovies.aithanasakis.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.popularmovies.aithanasakis.popularmovies.BuildConfig;
import com.popularmovies.aithanasakis.popularmovies.MyApplication;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieDBResponse;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;
import com.popularmovies.aithanasakis.popularmovies.network.MovieDBService;
import com.popularmovies.aithanasakis.popularmovies.repository.PopularMoviesRepository;

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

public class DetailsActivityViewModel extends ViewModel{
    private static boolean internetState = false;
    private static Movie selectedMovie;
    public static boolean isFavorite = true;
    private MediatorLiveData<List<MovieVideos>> videosListObservable;
    private MediatorLiveData<List<MovieReviews>> reviewsListObservable;
    @Inject
    public PopularMoviesRepository popularRepository;
    public DetailsActivityViewModel() {
        selectedMovie = new Movie(0,0,false,0.0,"",0.0,"",
                "","",null,"",false,"","");
        videosListObservable =  new MediatorLiveData<>();
        reviewsListObservable =  new MediatorLiveData<>();
        MyApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
    }

    public void storeFavorite(byte[] posterBlob ,byte[] backdropBlob ){
        popularRepository.storeFavorite(selectedMovie,posterBlob,backdropBlob);
    }

    public void deleteFavorite(){
        popularRepository.deleteFavorite(selectedMovie);
    }
    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;

    }

    public void requestMovieDetails(){
        reviewsListObservable.addSource(
                popularRepository.getMovieReviewsFromWeb(selectedMovie.getId(), BuildConfig.THEMOVIEDB_API_KEY),
                apiResponse -> reviewsListObservable.setValue(apiResponse)
        );
        videosListObservable.addSource(
                popularRepository.getMovieVideosFromWeb(selectedMovie.getId(), BuildConfig.THEMOVIEDB_API_KEY),
                apiResponse -> videosListObservable.setValue(apiResponse)
        );

        popularRepository.getMovieVideosFromWeb(selectedMovie.getId(), BuildConfig.THEMOVIEDB_API_KEY);
    }

    public LiveData<List<MovieVideos>> getVideosListObservable() {
        return videosListObservable;
    }

    public LiveData<List<MovieReviews>> getReviewsListObservable() {

        return reviewsListObservable;
    }

    public void setInternetState(boolean internetState) {
        this.internetState = internetState;
        Timber.v(String.valueOf(internetState));
    }
}
