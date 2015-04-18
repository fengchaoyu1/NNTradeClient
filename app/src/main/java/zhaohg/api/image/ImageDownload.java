package zhaohg.api.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dip on 2015/3/31.
 */
public class ImageDownload {
    private class ImageRequestTask extends AsyncTask<String, Integer, Bitmap> {
        protected Bitmap doInBackground(String ...imagePath) {
            HttpGet httpRequest = new HttpGet(imagePath[0]);
            HttpClient httpClient = new DefaultHttpClient();
            Bitmap bitmap = null;
            try {
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    InputStream is = httpEntity.getContent();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                }
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap bitmap) {
            //add excute
        }
    }

    public void getImageDownload(String webname){
        ImageRequestTask imageRequestTask = new ImageRequestTask();
        imageRequestTask.execute(webname);
    }

}
