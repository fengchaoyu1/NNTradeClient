package zhaohg.api.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dip on 2015/3/31.
 */
public class ImageThumbnails {
    public Bitmap getImageThumbnail(String imagePath,int width,int height){
        Bitmap bmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        bmap = BitmapFactory.decodeFile(imagePath,opt);
        opt.inJustDecodeBounds = false;

        int h = opt.outHeight;
        int w = opt.outWidth;
        int beHeight = h/height;
        int beWidth = w/width;

        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        opt.inSampleSize = be;
        bmap = BitmapFactory.decodeFile(imagePath,opt);
        bmap = ThumbnailUtils.extractThumbnail(bmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bmap;
    }
    public String saveImagePath(String imagePath , Bitmap bitmap){
        String thumbnailPath = imagePath +"_th"+ ".jpg";
        File file1 = new File(thumbnailPath);
        try{
            FileOutputStream out =new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return thumbnailPath;

    }

}
