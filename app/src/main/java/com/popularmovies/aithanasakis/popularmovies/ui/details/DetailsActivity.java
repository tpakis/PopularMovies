package com.popularmovies.aithanasakis.popularmovies.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.popularmovies.aithanasakis.popularmovies.R;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.DetailsActivityViewModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String BUNDLE_MOVIE = "item";
    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.nested_ScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindString(R.string.MOVIE_DB_BACKDROP_PATH)
    String movieDBImagePath;
    NetworkBroadcastReceiver mNetworkReceiver;
    IntentFilter mNetworkIntentFilter;
    DetailsFragment detailsFragment;
    Boolean isFavorite = false;
    private Movie selectedMovie;
    private DetailsActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        selectedMovie = getIntent().getExtras().getParcelable(BUNDLE_MOVIE);
        viewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);
        viewModel.setSelectedMovie(selectedMovie);

        Timber.d("Timber" + selectedMovie.toString());
        if (savedInstanceState == null) {
            detailsFragment = new DetailsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.details_fragment_container, detailsFragment)
                    .commit();
        }
        //change toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
        collapsingToolbarLayout.setTitle(selectedMovie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        viewModel.getReviewsListObservable().observe(DetailsActivity.this, new Observer<List<MovieReviews>>() {
            @Override
            public void onChanged(@Nullable List<MovieReviews> myMovieItemsList) {
                if (detailsFragment != null) {
                    detailsFragment.setRvReviewsResults(myMovieItemsList);
                }
            }
        });
        viewModel.getVideosListObservable().observe(DetailsActivity.this, new Observer<List<MovieVideos>>() {
            @Override
            public void onChanged(@Nullable List<MovieVideos> myMovieItemsList) {
                if (detailsFragment != null) {
                    detailsFragment.setRvVideosResults(myMovieItemsList);
                }
            }
        });
        viewModel.getIsFavorite().observe(DetailsActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFavorite) {
                changeFabIccn(isFavorite);
            }
        });
        viewModel.requestMovieDetails();
        //setup eplicit broadcast receiver
        mNetworkIntentFilter = new IntentFilter();
        mNetworkReceiver = new NetworkBroadcastReceiver();
        mNetworkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForInternet();
        //load backdrop photo

        if ((!viewModel.getInternetState()) && selectedMovie.getBackdropBlob() != null) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .dontTransform()
                    .placeholder(R.drawable.ic_public_black_24dp)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(backdrop.getContext()).load(selectedMovie.getBackdropBlob()).apply(options)
                    .into(backdrop);
        } else {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .dontTransform()
                    .placeholder(R.drawable.ic_public_black_24dp)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(backdrop.getContext()).load(movieDBImagePath + selectedMovie.getBackdropPath()).apply(options)
                    .into(backdrop);
        }
    }

    private void changeFabIccn(Boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite) {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

    }

    private void checkForInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        viewModel.setInternetState(netInfo != null && netInfo.isConnectedOrConnecting());
    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view) {
        if (detailsFragment != null) {
            viewModel.fabClicked(imageViewToByte(detailsFragment.detailsPoster), imageViewToByte(backdrop));
        }
    }

    private byte[] imageViewToByte(ImageView view) {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) view.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte;
    }

    private class NetworkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkForInternet();
        }
    }

}
