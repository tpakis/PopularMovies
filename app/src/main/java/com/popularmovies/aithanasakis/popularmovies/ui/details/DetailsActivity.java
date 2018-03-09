package com.popularmovies.aithanasakis.popularmovies.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.popularmovies.aithanasakis.popularmovies.R;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.DetailsActivityViewModel;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 28/02/2018.
 */

public class DetailsActivity extends AppCompatActivity {

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
    @BindString(R.string.MOVIE_DB_IMAGE_PATH)
    String movieDBImagePath;
    private Movie selectedMovie;
    private DetailsActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        selectedMovie = getIntent().getExtras().getParcelable("item");
        viewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);
        viewModel.setSelectedMovie(selectedMovie);
        Timber.d("Timber" + selectedMovie.toString());
        if (savedInstanceState == null) {
            DetailsFragment detailsFragment = new DetailsFragment();
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
        //load backdrop photo
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
