package zhaohg.test.helper;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class CreateImage {

    private String imagePath = "";
    private String thumbnailPath = "";

    public CreateImage() {
        try {
            this.createImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void createImage() throws Exception {
        imagePath = Environment.getExternalStorageDirectory() + File.separator + "test_image.jpg";
        Bitmap bitmap = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap.setPixel(i, j, Color.argb(255,
                        255 * i / bitmap.getWidth(),
                        255 - 255 * i / bitmap.getWidth(),
                        255 * j / bitmap.getHeight()));
            }
        }
        FileOutputStream outputStream = new FileOutputStream(imagePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
        thumbnailPath = Environment.getExternalStorageDirectory() + File.separator + "test_thumbnail.jpg";
        bitmap = Bitmap.createBitmap(200, 150, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap.setPixel(i, j, Color.argb(255,
                        255 * i / bitmap.getWidth(),
                        255 - 255 * i / bitmap.getWidth(),
                        255 * j / bitmap.getHeight()));
            }
        }
        outputStream = new FileOutputStream(thumbnailPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
    }
}

