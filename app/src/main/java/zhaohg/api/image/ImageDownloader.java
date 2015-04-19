package zhaohg.api.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<Void, Integer, Void> {
    private String url;
    private Context context;
    private ImageView img;
    private Bitmap bmp;
    public ImageDownloader(String url,ImageView img,Bitmap bmp) {
        this.url = url;
        this.img = img;
        this.bmp = bmp;
    }
    protected Void doInBackground(Void... arg0) {
        bmp = getBitmapFromURL(url);
        return null;
    }

    protected void onPostExecute(Void result) {
        img.setImageBitmap(bmp);
        super.onPostExecute(result);
    }
    public static Bitmap getBitmapFromURL(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
