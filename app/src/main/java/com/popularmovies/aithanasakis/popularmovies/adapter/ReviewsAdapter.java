package com.popularmovies.aithanasakis.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularmovies.aithanasakis.popularmovies.R;
import com.popularmovies.aithanasakis.popularmovies.model.MovieReviews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 3piCerberus on 07/03/2018.
 */
// getting ready for phase 2
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ResultsHolder> {
    private final ReviewsAdapterOnClickHandler mClickHandler;

    private List<MovieReviews> movieItemsResults;

    public ReviewsAdapter(ReviewsAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    @Override
    public ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_reviews_item, parent, false);

        return new ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsHolder holder, int position) {
        MovieReviews movieReview = movieItemsResults.get(position);
        holder.textContent.setText(movieReview.getContent());
        holder.textAuthor.setText(movieReview.getAuthor());

    }

    public void setReviewsResults(List<MovieReviews> movieItemsResults) {
        this.movieItemsResults = movieItemsResults;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (movieItemsResults == null) return 0;
        return movieItemsResults.size();
    }

    public interface ReviewsAdapterOnClickHandler {
        void onClick(MovieReviews selectedMovieItem);
    }

    public class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_author)
        TextView textAuthor;
        @BindView(R.id.text_content)
        TextView textContent;

        public ResultsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positionClicked = getAdapterPosition();
            MovieReviews selectedItem = movieItemsResults.get(positionClicked);
            mClickHandler.onClick(selectedItem);
        }
    }
}
