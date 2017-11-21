package com.example.android.booklistingapp;

//http://www.tutorialspoint.com/android/android_json_parser.htm
//https://developer.android.com/reference/org/json/JSONObject.html?utm_source=udacity&utm_medium=course&utm_campaign=android_basics
//https://developer.android.com/reference/org/json/JSONArray.html?utm_source=udacity&utm_medium=course&utm_campaign=android_basics

/**
 * An {@link BookListing} object contains information related to a single book.
 */
public class BookListing {

    /**
     * Title of the book
     */
    private String Title;

    /**
     * Author of the book
     */
    private String Authors;

    /**
     * Constructs a new {@link BookListing} object.
     *
     * @param title  is the title of the book
     * @param authors is the author of the book
     */
    public BookListing(String title, String authors) {
        Title = title;
        Authors = authors;
    }

    /**
     * Returns the title of the book.
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Returns the authors of the book.
     */
    public String getAuthors() {
        return Authors;
    }

}
