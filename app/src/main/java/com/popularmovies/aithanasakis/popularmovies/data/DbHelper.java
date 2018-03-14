package com.popularmovies.aithanasakis.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 3piCerberus on 14/03/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + MovieContract.MovieItem.TABLE_NAME + " ("
                + MovieContract.MovieItem.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + MovieContract.MovieItem.COLUMN_TITLE + " TEXT,"
                + MovieContract.MovieItem.COLUMN_OVERVIEW + " TEXT,"
                + MovieContract.MovieItem.COLUMN_VOTE_COUNT + " INTEGER NOT NULL DEFAULT 0, "
                + MovieContract.MovieItem.COLUMN_VOTE_AVERAGE + " REAL NOT NULL DEFAULT 0, "
                + MovieContract.MovieItem.COLUMN_POPULARITY + " REAL NOT NULL DEFAULT 0, "
                + MovieContract.MovieItem.COLUMN_GENRES_ID + " TEXT,"
                + MovieContract.MovieItem.COLUMN_VIDEO + " INTEGER NOT NULL DEFAULT 0, "
                + MovieContract.MovieItem.COLUMN_ADULT + " INTEGER NOT NULL DEFAULT 0, "
                + MovieContract.MovieItem.COLUMN_ORIGINAL_LANGUAGE + " TEXT,"
                + MovieContract.MovieItem.COLUMN_RELEASE_DATE + " TEXT,"
                + MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE + " TEXT,"
                + MovieContract.MovieItem.COLUMN_BACKDROP_BLOB + " BLOB,"
                + MovieContract.MovieItem.COLUMN_BACKDROP_PATH + " TEXT,"
                + MovieContract.MovieItem.COLUMN_POSTER_BLOB + " BLOB,"
                + MovieContract.MovieItem.COLUMN_POSTER_PATH + " TEXT" + ")";

        db.execSQL(CREATE_INVENTORY_TABLE);
        Log.d("DB Created succesfully", "Success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No need for an upgrade at this stage of the app
        Log.d("DB Updated succesfully", "Success");
    }
}
