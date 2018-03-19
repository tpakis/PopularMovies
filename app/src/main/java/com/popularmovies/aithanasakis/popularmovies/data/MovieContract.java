package com.popularmovies.aithanasakis.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by 3piCerberus on 14/03/2018.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.popularmovies.aithanasakis.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "favorites";

    //empty constructor private not to instantiate
    private MovieContract() {
    }

    public static final class MovieItem implements BaseColumns {
        public final static String TABLE_NAME = "favorites";
        /**
         * Name of the Product.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_ID = "id";

        /**
         * Title of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TITLE = "title";

        /**
         * Description (overview) of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_OVERVIEW = "overview";
        /**
         * The release date of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_RELEASE_DATE = "releasedate";
        /**
         * Vote count of the movie.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_VOTE_COUNT = "votecount";
        /**
         * Vote Average of the movie.
         * <p>
         * Type: REAL
         */
        public final static String COLUMN_VOTE_AVERAGE = "voteaverage";
        /**
         * Popularity of the movie.
         * <p>
         * Type: REAL
         */
        public final static String COLUMN_POPULARITY = "popularity";
        /**
         * Whether the movie has video or not.
         * <p>
         * Type: BOOLEAN
         */
        public final static String COLUMN_VIDEO = "video";
        /**
         * Whether it's an adult movie or not;
         * <p>
         * Type: BOOLEAN
         */
        public final static String COLUMN_ADULT = "adult";

        /**
         * Path of the poster of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_POSTER_PATH = "posterpath";
        /**
         * Path of the backdrop of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_BACKDROP_PATH = "backdroppath";
        /**
         * List of movie genres.
         * <p>
         * Type: BLOB
         */
        public final static String COLUMN_POSTER_BLOB = "posterblob";
        /**
         * Path of the backdrop of the movie.
         * <p>
         * Type: BLOB
         */
        public final static String COLUMN_BACKDROP_BLOB = "backdropblob";
        /**
         * List of movie genres.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_GENRES_ID = "genreslist";
        /**
         * The original title of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_ORIGINAL_TITLE = "originaltitle";
        /**
         * The original language of the movie.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_ORIGINAL_LANGUAGE = "originallanguage";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
    }

}
