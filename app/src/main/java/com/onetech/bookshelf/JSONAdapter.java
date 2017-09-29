package com.onetech.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by francatm0 on 26/09/17.
 */

public class JSONAdapter extends BaseAdapter {

    private static final String IMAGE_URL_BASE
            = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater layoutInflater) {
        mContext = context;
        mInflater = layoutInflater;
        mJsonArray = new JSONArray();
    }

    public void updateData(JSONArray jsonArray) {

        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_book, null);

            // create a new "Holder" with subviews
            viewHolder = new ViewHolder();
            viewHolder.mThumbnailImageView =
                    (ImageView) convertView
                            .findViewById(R.id.img_thumbnail);
            viewHolder.mTitleTextView =
                    (TextView) convertView
                            .findViewById(R.id.text_title);
            viewHolder.mAuthorTextView =
                    (TextView) convertView
                            .findViewById(R.id.text_author);

            // hang onto this holder for future recyclage
            convertView.setTag(viewHolder);
        } else {
            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        JSONObject jsonObject = getItem(position);

        // See if there is a cover ID in the Object
        if (jsonObject.has("cover_i")) {

            // If so, grab the Cover ID out from the object
            String imageID = jsonObject.optString("cover_i");

            // Construct the image URL (specific to API)
            String imageURL = IMAGE_URL_BASE
                    + imageID
                    + "-S.jpg";

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext)
                    .load(imageURL)
                    .placeholder(R.drawable.ic_books)
                    .into(viewHolder.mThumbnailImageView);
        } else {

            // If there is no cover ID in the object, use a placeholder
            viewHolder.mThumbnailImageView
                    .setImageResource(R.drawable.ic_books);
        }

        // Grab the title and author from the JSON
        String bookTitle = "";
        String authorName = "";

        if (jsonObject.has("title")) {
            bookTitle = jsonObject.optString("title");
        }

        if (jsonObject.has("author_name")) {
            authorName = jsonObject.optJSONArray("author_name")
                    .optString(0);
        }

        // Send these Strings to the TextViews for display
        viewHolder.mTitleTextView.setText(bookTitle);
        viewHolder.mAuthorTextView.setText(authorName);

        return convertView;
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView mThumbnailImageView;
        public TextView mTitleTextView;
        public TextView mAuthorTextView;
    }
}
