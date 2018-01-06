package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import static com.example.android.booklistingapp.QueryUtils.fetchBookListingData;

/**
 * Loads a list by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class BookListingLoader extends AsyncTaskLoader<List<BookListing>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookListingLoader.class.getName();

    //TODO Update the Query URL based on the SearchView Search String

    /** Query URL */
    private String Url;

    /**
     * Constructs a new {@link BookListingLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookListingLoader(Context context, String url) {
        super(context);
        Url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<BookListing> loadInBackground() {
        if (Url == null) {
            return null;
        }

        //Perform the network request, parse the response, and extract a list of books.
        return QueryUtils.fetchBookListingData(Url);
    }
}
