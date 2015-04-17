package zhaohg.api.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageThumbnails {

    private static final int THUMBNAIL_WIDTH = 400;
    private static final int THUMBNAIL_HEIGHT = 300;

    public Bitmap getThumbnail(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        return ThumbnailUtils.extractThumbnail(bitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
    }

    public String saveImage(String imagePath, Bitmap bitmap){
        String thumbnailPath = imagePath + "_th"+ ".jpg";
        File thumbnailFile = new File(thumbnailPath);
        try {
            FileOutputStream out =new FileOutputStream(thumbnailFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbnailPath;
    }

}
