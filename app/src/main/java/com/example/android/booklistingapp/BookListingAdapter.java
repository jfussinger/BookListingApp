package com.example.android.booklistingapp;

//https://github.com/bumptech/glide

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * An {@link BookListingAdapter} knows how to create a list item layout for each book
 * in the data source (a list of {@link BookListing} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */


public class BookListingAdapter extends ArrayAdapter<BookListing> {

    //Context context;

    private static class ViewHolder {

        TextView title;
        TextView authors;
        TextView publishedDate;
        ImageView thumbnail;
        TextView textSnippet;
    }

    /**
     * Constructs a new {@link BookListingAdapter}.
     *
     * @param context of the app
     * @param booklistings is the list of books, which is the data source of the adapter
     */
    public BookListingAdapter(Context context, List<BookListing> booklistings) {
        super(context, 0, booklistings);
    }

    /**
     * Returns a list item view that displays information about the book
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.

        ViewHolder viewHolder;
        BookListing BookListing = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.booklisting_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.authors = (TextView) convertView.findViewById(R.id.authors);
            viewHolder.publishedDate = (TextView) convertView.findViewById(R.id.publishedDate);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.textSnippet = (TextView) convertView.findViewById(R.id.textSnippet);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(BookListing.getTitle());
        viewHolder.authors.setText(BookListing.getAuthors());
        viewHolder.publishedDate.setText(BookListing.getPublishedDate());

        final String thumbnail = BookListing.getThumbnail();
        Glide.with(getContext()).load(thumbnail).placeholder(R.drawable.ic_gb).into(viewHolder.thumbnail);

        viewHolder.textSnippet.setText(BookListing.getTextSnippet());


        return convertView;
    }
}
