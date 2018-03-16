package com.popularmovies.aithanasakis.popularmovies.adapter;

import android.content.Context;
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
import com.popularmovies.aithanasakis.popularmovies.model.MovieVideos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 3piCerberus on 07/03/2018.
 */
// getting ready for phase 2
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ResultsHolder> {
    private final VideosAdapterOnClickHandler mClickHandler;
    private final Context context;
    private List<MovieVideos> movieItemsResults;
    private static final String YOUTUBE = "https://img.youtube.com/vi/%s/mqdefault.jpg";
    public VideosAdapter(VideosAdapterOnClickHandler handler, Context context) {
        mClickHandler = handler;
        this.context = context;
    }

    @Override
    public ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_videos_item, parent, false);

        return new ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsHolder holder, int position) {
        MovieVideos movieVideo = movieItemsResults.get(position);
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .dontTransform()
                .placeholder(R.drawable.ic_public_black_24dp)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(String.format(YOUTUBE, movieVideo.getKey())).apply(options)
                .into(holder.videoImage);
    }

    public void setVideosResults(List<MovieVideos> movieItemsResults) {
        this.movieItemsResults = movieItemsResults;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (movieItemsResults == null) return 0;
        return movieItemsResults.size();
    }

    public interface VideosAdapterOnClickHandler {
        void onClick(MovieVideos selectedMovieItem);
    }

    public class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_video_image)
        ImageView videoImage;


        public ResultsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positionClicked = getAdapterPosition();
            MovieVideos selectedItem = movieItemsResults.get(positionClicked);
            mClickHandler.onClick(selectedItem);
        }
    }
}
