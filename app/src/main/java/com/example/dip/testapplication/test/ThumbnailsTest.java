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
import com.example.dip.testapplication.Creatpicture;


import static junit.framework.Assert.assertFalse;


public class ThumbnailsTest extends InstrumentationTestCase{
    private Bitmap mBitmapBig = null;
    private Bitmap mBitmapSmall = null;



    public Bitmap getImageThumbnail(String imagePath,int width,int height){
        Bitmap bmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        bmap=BitmapFactory.decodeFile(imagePath,opt);
        opt.inJustDecodeBounds=false;

        int h = opt.outHeight;
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
        return bmap;
    }


    public void testThumbnailsBig() throws Exception {
        Creatpicture creat= new Creatpicture();

        Bitmap mBitmap=null;
        String ImagePath=Environment.getExternalStorageDirectory() + "/"+"abc" + ".jpg";
        mBitmap=getImageThumbnail(ImagePath,600,700);
        File file = new File(Environment.getExternalStorageDirectory() +"/", "ooo" + ".jpg");
        FileOutputStream output =new FileOutputStream(file);
        try {

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, output);
            output.flush();
            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(output.equals(null));

    }

    public void testThumbnailsSmall() throws Exception{
        /*int[] mColors;
        int WIDTH=75;
        int HEIGHT=100;
        int STRIDE=150;//must be >=WIDTH
        String IMAGENAME="abc";
        Bitmap mBitmap=null;

        mColors=initColors(STRIDE,HEIGHT,WIDTH);
        mBitmapSmall=Bitmap.createBitmap(mColors, 0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        File file1 = new File(Environment.getExternalStorageDirectory() + "/"+IMAGENAME + ".jpg");
        FileOutputStream out =new FileOutputStream(file1);
        try {

            mBitmapSmall.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(out.equals(null));*/
        Creatpicture creat= new Creatpicture();
        Bitmap mBitmap=null;
        String ImagePath=Environment.getExternalStorageDirectory() + "/"+"abc" + ".jpg";
        mBitmap=getImageThumbnail(ImagePath, 150, 200);
        File file = new File(Environment.getExternalStorageDirectory() + "/","ooo" + ".jpg");
        FileOutputStream output =new FileOutputStream(file);
        try {

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, output);
            output.flush();
            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(output.equals(null));
    }
}
