package com.example.tskysp.ebaysearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by TskySP on 23/04/15.
 */
class getImageFromURL extends AsyncTask<String, Void, Bitmap> {
    ImageView image;

    public getImageFromURL(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap imageFrom = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            imageFrom = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return imageFrom;
    }

    protected void onPostExecute(Bitmap result) {
        image.setImageBitmap(result);
    }
}