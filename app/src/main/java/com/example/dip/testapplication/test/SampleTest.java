package com.example.dip.testapplication.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dip on 2015/3/19.
 */
public class SampleTest extends InstrumentationTestCase{

    public  void testThumbnails(){

        String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        String imagePath = path + "adc.jpg";
        Bitmap bmap=null;
        BitmapFactory.Options opt=new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        bmap=BitmapFactory.decodeFile(imagePath,opt);
        opt.inJustDecodeBounds=false;

        int width=150;
        int height=200;
        int h=opt.outHeight;
        int w=opt.outWidth;
        int beHeight=h/height;
        int beWidth=w/width;

        int be=1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        opt.inSampleSize=be;
        bmap=BitmapFactory.decodeFile(imagePath,opt);
        bmap= ThumbnailUtils.extractThumbnail(bmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        File file = new File(path + "ooo" + ".jpg");

        try{
            FileOutputStream out =new FileOutputStream(file);
            bmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
