package com.popularmovies.aithanasakis.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.popularmovies.aithanasakis.popularmovies.MyApplication;
import com.popularmovies.aithanasakis.popularmovies.data.MovieContract.MovieItem;


import javax.inject.Inject;


/**
 * Created by 3piCerberus on 14/03/2018.
 */

public class LocalDBContentProvider extends ContentProvider{

    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String LOG_TAG =  LocalDBContentProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_ITEMS, ITEMS);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_ITEMS + "/#", ITEM_ID);
    }
    @Inject
    public DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
       // mDbHelper = new DbHelper(getContext());
        return true;
    }
//should be called in everymethod it needs the mDbHelper otherwise the injection won't work
    private void deferInit(){
       if (mDbHelper==null) {
           MyApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
       }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        deferInit();
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = database.query(MovieItem.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = MovieItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieItem.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {
        deferInit();
        Integer movieId = values.getAsInteger(MovieItem.COLUMN_ID);
        if (movieId != null && movieId < 0) {
            throw new IllegalArgumentException("Item requires an id");
        }
        String title = values.getAsString(MovieItem.COLUMN_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Item requires a title");
        }
        String overview = values.getAsString(MovieItem.COLUMN_OVERVIEW);
        if (overview == null) {
            throw new IllegalArgumentException("Item requires a title");
        }
        String releasedate = values.getAsString(MovieItem.COLUMN_RELEASE_DATE);
        if (releasedate == null) {
            throw new IllegalArgumentException("Item requires a Release Date");
        }
        Integer quantity = values.getAsInteger(MovieItem.COLUMN_VOTE_COUNT);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Item requires valid vote count");
        }
        Double average = values.getAsDouble(MovieItem.COLUMN_VOTE_AVERAGE);
        if (average != null && average < 0) {
            throw new IllegalArgumentException("Item requires valid vote average");
        }
        Double popularity = values.getAsDouble(MovieItem.COLUMN_POPULARITY);
        if (popularity != null && popularity < 0) {
            throw new IllegalArgumentException("Item requires valid vote average");
        }
        Integer video = values.getAsInteger(MovieItem.COLUMN_VIDEO);
        if (video != null && video < 0) {
            throw new IllegalArgumentException("Item requires valid video booleam");
        }
        Integer adult = values.getAsInteger(MovieItem.COLUMN_ADULT);
        if (adult != null && adult < 0) {
            throw new IllegalArgumentException("Item requires valid adult booleam");
        }
        String genres = values.getAsString(MovieItem.COLUMN_GENRES_ID);
        if (genres == null) {
            throw new IllegalArgumentException("Item requires genres list");
        }
        String originalTitle = values.getAsString(MovieItem.COLUMN_ORIGINAL_TITLE);
        if (originalTitle == null) {
            throw new IllegalArgumentException("Item requires an original title");
        }
        String originalLanguage = values.getAsString(MovieItem.COLUMN_ORIGINAL_LANGUAGE);
        if (originalLanguage == null) {
            throw new IllegalArgumentException("Item requires an original language");
        }
        String posterPath = values.getAsString(MovieItem.COLUMN_POSTER_PATH);
        if (posterPath == null) {
            throw new IllegalArgumentException("Item requires poster path");
        }
        String posterBlob = values.getAsString(MovieItem.COLUMN_POSTER_BLOB);
        if (posterBlob == null) {
            throw new IllegalArgumentException("Item requires poster blob");
        }
        String backdropPath = values.getAsString(MovieItem.COLUMN_BACKDROP_PATH);
        if (backdropPath == null) {
            throw new IllegalArgumentException("Item requires backdrop path");
        }
        String backdropBlob = values.getAsString(MovieItem.COLUMN_BACKDROP_BLOB);
        if (backdropBlob == null) {
            throw new IllegalArgumentException("Item requires backdrop blob");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(MovieItem.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                selection = MovieItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        deferInit();
       if (values.containsKey(MovieItem.COLUMN_ID)) {
           Integer movieId = values.getAsInteger(MovieItem.COLUMN_ID);
           if (movieId != null && movieId < 0) {
               throw new IllegalArgumentException("Item requires an id");
           }
       }
        if (values.containsKey(MovieItem.COLUMN_TITLE)) {
            String title = values.getAsString(MovieItem.COLUMN_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Item requires a title");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_OVERVIEW)) {
            String overview = values.getAsString(MovieItem.COLUMN_OVERVIEW);
            if (overview == null) {
                throw new IllegalArgumentException("Item requires a title");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_RELEASE_DATE)) {
            String releasedate = values.getAsString(MovieItem.COLUMN_RELEASE_DATE);
            if (releasedate == null) {
                throw new IllegalArgumentException("Item requires a Release Date");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_VOTE_COUNT)) {
            Integer quantity = values.getAsInteger(MovieItem.COLUMN_VOTE_COUNT);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Item requires valid vote count");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_VOTE_AVERAGE)) {
            Double average = values.getAsDouble(MovieItem.COLUMN_VOTE_AVERAGE);
            if (average != null && average < 0) {
                throw new IllegalArgumentException("Item requires valid vote average");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_POPULARITY)) {
            Double popularity = values.getAsDouble(MovieItem.COLUMN_POPULARITY);
            if (popularity != null && popularity < 0) {
                throw new IllegalArgumentException("Item requires valid vote average");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_VIDEO)) {
            Integer video = values.getAsInteger(MovieItem.COLUMN_VIDEO);
            if (video != null && video < 0) {
                throw new IllegalArgumentException("Item requires valid video booleam");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_ADULT)) {
            Integer adult = values.getAsInteger(MovieItem.COLUMN_ADULT);
            if (adult != null && adult < 0) {
                throw new IllegalArgumentException("Item requires valid adult booleam");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_GENRES_ID)) {
            String genres = values.getAsString(MovieItem.COLUMN_GENRES_ID);
            if (genres == null) {
                throw new IllegalArgumentException("Item requires genres list");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_ORIGINAL_TITLE)) {
            String originalTitle = values.getAsString(MovieItem.COLUMN_ORIGINAL_TITLE);
            if (originalTitle == null) {
                throw new IllegalArgumentException("Item requires an original title");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_ORIGINAL_LANGUAGE)) {
            String originalLanguage = values.getAsString(MovieItem.COLUMN_ORIGINAL_LANGUAGE);
            if (originalLanguage == null) {
                throw new IllegalArgumentException("Item requires an original language");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_POSTER_PATH)) {
            String posterPath = values.getAsString(MovieItem.COLUMN_POSTER_PATH);
            if (posterPath == null) {
                throw new IllegalArgumentException("Item requires poster path");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_POSTER_BLOB)) {
            String posterBlob = values.getAsString(MovieItem.COLUMN_POSTER_BLOB);
            if (posterBlob == null) {
                throw new IllegalArgumentException("Item requires poster blob");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_BACKDROP_PATH)) {
            String backdropPath = values.getAsString(MovieItem.COLUMN_BACKDROP_PATH);
            if (backdropPath == null) {
                throw new IllegalArgumentException("Item requires backdrop path");
            }
        }
        if (values.containsKey(MovieItem.COLUMN_BACKDROP_BLOB)) {
            String backdropBlob = values.getAsString(MovieItem.COLUMN_BACKDROP_BLOB);
            if (backdropBlob == null) {
                throw new IllegalArgumentException("Item requires backdrop blob");
            }
        }


        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(MovieItem.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        deferInit();
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                rowsDeleted = database.delete(MovieItem.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = MovieItem.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MovieItem.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return MovieItem.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return MovieItem.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

