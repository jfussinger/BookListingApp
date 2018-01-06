package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving book listing data from Google Books.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google Books dataset and return a list of {@link BookListing} objects.
     */
    public static List<BookListing> fetchBookListingData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link BookListing}s
        List<BookListing> booklistings = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link BookListing}s
        return booklistings;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link BookListing} objects that has been built up from
     * parsing the given JSON response.
     */

    private static List<BookListing> extractFeatureFromJson(String JSON) {

        // If the JSON string is empty or null, then return early.

        if (TextUtils.isEmpty(JSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to

        List<BookListing> booklistings = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {

            JSONObject jsonObject = new JSONObject(JSON);

            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // For each book in the booklistingArray, create an {@link BookListing} object

            for (int i = 0; i < itemsArray.length(); i++) {

                // Get a single book at position i within the list of books

                JSONObject currentBookListing = itemsArray.getJSONObject(i);

                JSONObject volumeInfo = currentBookListing.getJSONObject("volumeInfo");

                String title = "";
                String authors = "";
                String publishedDate = "";

                if(volumeInfo.has("title")) {
                    // Extract the value for the key called "title"
                    title = volumeInfo.getString("title");

                }
                else {
                    title = "No Title!";
                }

                if(volumeInfo.has("authors")) {
                    // Extract the value for the key called "authors"
                  authors = volumeInfo.getString("authors");

                }
                else {
                    authors = "No Author!";
                }

                if(volumeInfo.has("publishedDate")) {
                    // Extract the value for the key called "publishedDate"
                    publishedDate = volumeInfo.getString("publishedDate");

                }
                else {
                    publishedDate = "No Published Date!";
                }

                //Extract the JSONObject for the key called "imageLinks" for the Thumbnail

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                String thumbnail = "";

                if (imageLinks.has("thumbnail")) {
                    // Extract the value for the key called "thumbnail"
                    thumbnail = imageLinks.getString("thumbnail");

                } else {
                    thumbnail = "No Thumbnail!";
                }

                //Extract the JSONObject for the key called "searchInfo"

                JSONObject searchInfo = currentBookListing.getJSONObject("searchInfo");

                String textSnippet = "";

                if (searchInfo.has("textSnippet")) {
                    // Extract the value for the key called "textSnippet"
                    textSnippet = searchInfo.getString("textSnippet");

                } else {
                    textSnippet = "No TextSnippet!";
                }

                // Create a new {@link BookListing} object with the title, author, publishedDate, thumbnail, textSnippet
                // from the JSON response.

                BookListing booklisting = new BookListing(title, authors, publishedDate, thumbnail, textSnippet);

                // Add the new {@link BookListing} to the list of books.

                booklistings.add(booklisting);

            }

        } catch (JSONException e) {

            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.

            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of books

        return booklistings;
    }

}
