package com.popularmovies.aithanasakis.popularmovies.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.popularmovies.aithanasakis.popularmovies.R;
import com.popularmovies.aithanasakis.popularmovies.adapter.ReviewsAdapter;
import com.popularmovies.aithanasakis.popularmovies.adapter.StaggeredMoviesAdapter;
import com.popularmovies.aithanasakis.popularmovies.adapter.VideosAdapter;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;
import com.popularmovies.aithanasakis.popularmovies.ui.main.MainActivity;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.DetailsActivityViewModel;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 06/03/2018.
 */

public class DetailsFragment extends Fragment implements ReviewsAdapter.ReviewsAdapterOnClickHandler,
        VideosAdapter.VideosAdapterOnClickHandler {

    @BindView(R.id.details_poster)
    ImageView detailsPoster;
    @BindView(R.id.details_title)
    TextView detailsTitle;
    @BindView(R.id.details_ratingbar)
    RatingBar detailsRatingbar;
    @BindView(R.id.details_rating)
    TextView detailsRating;
    @BindView(R.id.details_votes)
    TextView detailsVotes;
    @BindView(R.id.details_release_date)
    TextView detailsReleaseDate;
    @BindView(R.id.card_showlight)
    CardView cardShowlight;
    @BindView(R.id.details_overview_label)
    TextView detailsOverviewLabel;
    @BindView(R.id.details_overview_text)
    TextView detailsOverviewText;
    @BindView(R.id.details_videos_label)
    TextView detailsVideosLabel;
    @BindView(R.id.details_videos_rv)
    RecyclerView detailsVideosRv;
    @BindView(R.id.details_reviews_label)
    TextView detailsReviewsLabel;
    @BindView(R.id.details_reviews_rv)
    RecyclerView detailsReviewsRv;
    @BindView(R.id.card_details)
    CardView cardDetails;
    @BindString(R.string.MOVIE_DB_IMAGE_PATH)
    String movieDBImagePath;
    Unbinder unbinder;
    private DetailsActivityViewModel viewModel;
    private DetailsActivity parent;
    private Movie selectedMovie;
    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManagerVideos;

    public DetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DetailsActivity) this.getActivity();
        viewModel = ViewModelProviders.of(parent).get(DetailsActivityViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //getting the selected movie from the details activity viewmodel

        selectedMovie = viewModel.getSelectedMovie();
        View viewgroup = inflater.inflate(R.layout.details_fragment, container, false);
        unbinder = ButterKnife.bind(this, viewgroup);
        detailsTitle.setText(selectedMovie.getTitle());

        if ((!viewModel.getInternetState()) && selectedMovie.getPosterBlob() != null) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .dontTransform()
                    .placeholder(R.drawable.ic_public_black_24dp)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(detailsPoster.getContext()).load(selectedMovie.getPosterBlob()).apply(options)
                    .into(detailsPoster);
        } else {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .dontTransform()
                    .placeholder(R.drawable.ic_public_black_24dp)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(detailsPoster.getContext()).load(movieDBImagePath + selectedMovie.getPosterPath()).apply(options)
                    .into(detailsPoster);
        }

        detailsRatingbar.setRating(selectedMovie.getVoteAverage().floatValue());
        detailsRating.setText("tmdb Rating: " + selectedMovie.getVoteAverage().toString());
        detailsVotes.setText("Votes: " + selectedMovie.getVoteCount().toString());
        detailsReleaseDate.setText("Release Date: " + selectedMovie.getReleaseDate());
        detailsOverviewText.setText(selectedMovie.getOverview());

        //recyclerview Reviews,Videos
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManagerVideos = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        detailsReviewsRv.setLayoutManager(mLinearLayoutManager);
        detailsVideosRv.setLayoutManager(mLinearLayoutManagerVideos);
        mVideosAdapter = new VideosAdapter(DetailsFragment.this, getContext());
        mReviewsAdapter = new ReviewsAdapter(DetailsFragment.this);
        detailsVideosRv.setAdapter(mVideosAdapter);
        detailsReviewsRv.setAdapter(mReviewsAdapter);

        return viewgroup;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(MovieReviews selectedReviewItem) {
        if (selectedReviewItem.getUrl() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedReviewItem.getUrl()));
            startActivity(intent);
        }
    }

    @Override
    public void onClick(MovieVideos selectedVideoItem) {
        if (selectedVideoItem.getKey() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + selectedVideoItem.getKey()));
            startActivity(intent);
        }
    }

    public void setRvReviewsResults(@Nullable List<MovieReviews> myMovieItemsList) {
        mReviewsAdapter.setReviewsResults(myMovieItemsList);
    }

    public void setRvVideosResults(@Nullable List<MovieVideos> myMovieItemsList) {
        mVideosAdapter.setVideosResults(myMovieItemsList);
    }
}
