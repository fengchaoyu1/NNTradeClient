package com.example.dip.testapplication;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dip on 2015/3/23.
 */
public class Creatpicture {
    public Creatpicture(){
        Bitmap mBitmapBig = null;
        int[] mColors;
        int WIDTH=300;
        int HEIGHT=400;
        int STRIDE=500;//must be >=WIDTH
        String IMAGE_NAME="abc";
        Bitmap mBitmap=null;

        mColors=initColors(STRIDE,HEIGHT,WIDTH);
        mBitmapBig=Bitmap.createBitmap(mColors, 0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        File file1 = new File(Environment.getExternalStorageDirectory() + "/"+IMAGE_NAME + ".jpg");
        FileOutputStream out=null;
        try {
            out = new FileOutputStream(file1);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            mBitmapBig.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        }  catch (IOException e) {
            e.printStackTrace();
        }

    }
    public int[] initColors(int STRIDE, int HEIGHT, int WIDTH) {
        int[] colors=new int[STRIDE*HEIGHT];
        for (int y = 0; y < HEIGHT; y++) {//use of x,y is legible then the use of i,j
            for (int x = 0; x < WIDTH; x++) {
                int r = x * 255 / (WIDTH - 1);
                int g = y * 255 / (HEIGHT - 1);
                int b = 255 - Math.min(r, g);
                int a = Math.max(r, g);
                colors[y*STRIDE + x]=(a<<24)|(r<<16)|(g<<8)|(b);//the shift operation generates the color ARGB
            }
        }
        return colors;
    }
}
