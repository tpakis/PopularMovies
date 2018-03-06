package com.popularmovies.aithanasakis.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.popularmovies.aithanasakis.popularmovies.R;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 3piCerberus on 06/03/2018.
 */

public class StaggeredMoviesAdapter extends RecyclerView.Adapter<StaggeredMoviesAdapter.ResultsHolder> {
    private final MovieDBResultsAdapterOnClickHandler mClickHandler;
    private List<Movie> movieItemsResults;
    private static final String MOVIE_DB_IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";

    public StaggeredMoviesAdapter(MovieDBResultsAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    @Override
    public ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);

        return new ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsHolder holder, int position) {
        Movie movieItem = movieItemsResults.get(position);
        holder.itemName.setText(movieItem.getTitle());
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .dontTransform()
                .placeholder(R.drawable.ic_public_black_24dp)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(holder.itemPhoto.getContext()).load(MOVIE_DB_IMAGE_PATH+movieItem.getPosterPath()).apply(options).into(holder.itemPhoto);

    }

    @Override
    public int getItemCount() {
        if (movieItemsResults == null) return 0;
        return movieItemsResults.size();
    }

    public void setMonieRvResults(List<Movie> movieItemsResults) {
        this.movieItemsResults = movieItemsResults;
        notifyDataSetChanged();
    }

    public interface MovieDBResultsAdapterOnClickHandler {
        void onClick(Movie selectedMovieItem);
    }

    public class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_item_name)
        public TextView itemName;

        @BindView(R.id.movie_photo)
        public ImageView itemPhoto;

        public ResultsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positionClicked = getAdapterPosition();
            Movie selectedItem = movieItemsResults.get(positionClicked);
            mClickHandler.onClick(selectedItem);
        }
    }
}