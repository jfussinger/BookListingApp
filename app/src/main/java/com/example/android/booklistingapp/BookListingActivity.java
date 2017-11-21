package com.example.android.booklistingapp;

//https://github.com/crlsndrsjmnz/MySearchViewExample
//https://developer.android.com/reference/android/widget/SearchView.html

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookListingActivity extends AppCompatActivity
        implements LoaderCallbacks<List<BookListing>> {

    private static final String LOG_TAG = BookListingActivity.class.getName();

    /**
     * URL for google books data from the Google Books dataset
     */
    String searchQuery;
    private final String url =
            "https://www.googleapis.com/books/v1/volumes?maxResults=20&q="+searchQuery;

    /**
     * Constant value for the book listing loader ID. We can choose any integer.
     */
    private static final int BOOKLISTING_LOADER_ID = 1;

    /**
     * Adapter for the list of books
     */
    private BookListingAdapter Adapter;

    SearchView searchView;

    //TO DO

    // Add a searchview
    // Take the search query from search view and update the URL
    // start the loader on click of search icon

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView EmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklisting_activity);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String Query) {
                Toast.makeText(getBaseContext(), searchQuery, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getBaseContext(), searchQuery, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // Find a reference to the {@link ListView} in the layout
        ListView booklistingListView = (ListView) findViewById(R.id.list);

        EmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booklistingListView.setEmptyView(EmptyStateTextView);

        // Create a new adapter that takes an empty list of google books as input
        Adapter = new BookListingAdapter(this, new ArrayList<BookListing>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booklistingListView.setAdapter(Adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOKLISTING_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            EmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<BookListing>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookListingLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<BookListing>> loader, List<BookListing> booklistings) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        EmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous book data
        Adapter.clear();

        // If there is a valid list of {@link BookListing}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (booklistings != null && !booklistings.isEmpty()) {
            Adapter.addAll(booklistings);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookListing>> loader) {
        // Loader reset, so we can clear out our existing data.
        Adapter.clear();
    }
}
