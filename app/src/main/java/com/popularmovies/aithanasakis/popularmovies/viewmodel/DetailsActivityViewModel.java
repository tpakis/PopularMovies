package com.popularmovies.aithanasakis.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.popularmovies.aithanasakis.popularmovies.BuildConfig;
import com.popularmovies.aithanasakis.popularmovies.MyApplication;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;
import com.popularmovies.aithanasakis.popularmovies.repository.PopularMoviesRepository;


import java.util.List;

import android.arch.lifecycle.Observer;
import android.util.Log;

import javax.inject.Inject;


import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class DetailsActivityViewModel extends ViewModel {
    private static boolean internetState = false;
    private static Movie selectedMovie;
    @Inject
    public PopularMoviesRepository popularRepository;
    private int videosRvScrollPosition;
    private int reviewsRvScrollPosition;
    private MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    //mediator lists because they transfer the changes of the livedata lists of the repository
    private MediatorLiveData<List<MovieVideos>> videosListObservable;
    private MediatorLiveData<List<MovieReviews>> reviewsListObservable;

    public DetailsActivityViewModel() {
        selectedMovie = new Movie(0, 0, false, 0.0, "", 0.0, "",
                "", "", null, "", false, "", "", null, null);
        videosListObservable = new MediatorLiveData<>();
        reviewsListObservable = new MediatorLiveData<>();
        isFavorite.setValue(false);
        MyApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
        videosListObservable.addSource(popularRepository.getLiveDataVideos(), new Observer<List<MovieVideos>>() {
            @Override
            public void onChanged(@Nullable List<MovieVideos> videos) {
                videosListObservable.setValue(videos);
            }
        });
        reviewsListObservable.addSource(popularRepository.getLiveDataReviews(), new Observer<List<MovieReviews>>() {
            @Override
            public void onChanged(@Nullable List<MovieReviews> reviews) {
                reviewsListObservable.setValue(reviews);
            }
        });
    }

    public int getVideosRvScrollPosition() {
        return videosRvScrollPosition;
    }

    public void setVideosRvScrollPosition(int moviesRvScrollPosition) {
        this.videosRvScrollPosition = moviesRvScrollPosition;
    }

    public int getReviewsRvScrollPosition() {
        return reviewsRvScrollPosition;
    }

    public void setReviewsRvScrollPosition(int reviewsRvScrollPosition) {
        this.reviewsRvScrollPosition = reviewsRvScrollPosition;
    }

    public void storeFavorite(byte[] posterBlob, byte[] backdropBlob) {
        selectedMovie.setBackdropBlob(backdropBlob);
        selectedMovie.setPosterBlob(posterBlob);
        popularRepository.storeFavorite(selectedMovie);
        isFavorite.setValue(true);
    }

    public void deleteFavorite() {
        popularRepository.deleteFavorite(selectedMovie);
        isFavorite.setValue(false);
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
        //postvalue asynchronus set value ui thread
        isFavorite.postValue(popularRepository.isFavorite(selectedMovie));

    }
    // request the repository to update it's data which we observer
    public void requestMovieDetails() {
        popularRepository.getMovieReviewsFromWeb(selectedMovie.getId(), BuildConfig.THEMOVIEDB_API_KEY);
        popularRepository.getMovieVideosFromWeb(selectedMovie.getId(), BuildConfig.THEMOVIEDB_API_KEY);
    }

    /**
     *
     * @return The list we should observer
     */
    public LiveData<List<MovieVideos>> getVideosListObservable() {
        return videosListObservable;
    }
    /**
     *
     * @return The list we should observer
     */
    public LiveData<List<MovieReviews>> getReviewsListObservable() {

        return reviewsListObservable;
    }

    public LiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public boolean getInternetState() {
        return internetState;
    }

    public void setInternetState(boolean internetState) {
        this.internetState = internetState;
        Timber.v(String.valueOf(internetState));
    }

    public void fabClicked(byte[] posterBlob, byte[] backdropBlob) {
        if (isFavorite.getValue()) {
            deleteFavorite();
        } else {
            storeFavorite(posterBlob, backdropBlob);
        }
    }

}
