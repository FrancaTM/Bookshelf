package com.onetech.bookshelf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ShareActionProvider

import com.squareup.picasso.Picasso

/**
 * Created by francatm0 on 27/09/17.
 */

class DetailActivity : Activity() {
    internal var mImageURL = ""
    internal var mShareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail)

        // Enable the "Up" button for more navigation options
        if (null != actionBar) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        // Access the imageview from XML
        val imageView = findViewById<View>(R.id.img_cover) as ImageView

        // 13. unpack the coverId from its trip inside your Intent
        val coverId = this.intent.extras!!.getString("coverID")

        // See if there is a valid coverId

        if (coverId != null && coverId.length > 0) {

            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverId + "-L.jpg"

            // Use Picasso to load the image
            Picasso.with(this)
                    .load(mImageURL)
                    .placeholder(R.drawable.img_books_loading)
                    .into(imageView)
        }
    }

    private fun setShareIntent() {
        // create an Intent with the contents of the TextView
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Book Recommendation!")
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageURL)

        // Make sure the provider knows
        // it should work with that Intent
        mShareActionProvider!!.setShareIntent(shareIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu
        // this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // Access the Share Item defined in menu XML
        val shareItem = menu.findItem(R.id.menu_item_share)

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider = shareItem.actionProvider as ShareActionProvider
        }

        setShareIntent()

        return true
    }

    companion object {

        private val IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/"
    }
}
