package com.example.dip.testapplication;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.ImageView;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.Dialog;
import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.app.Activity;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.util.Log;




public class MainActivity extends ActionBarActivity {
    Button btn =null;
    ImageView myView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* try {

            saveMyBitmap("ooo");
        } catch (IOException e) {

            e.printStackTrace();

        }



        String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        String name1 = path + "adc.jpg";

        Bitmap finBitmap=getImageThumbnail(name1,200,150);

        File file = new File(path + "ooo" + ".jpg");

        try{
            FileOutputStream out =new FileOutputStream(file);
            finBitmap.compress(Bitmap.CompressFormat.JPEG,30,out);
            out.flush();
            out.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }*/








        /*AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setMessage("nihao").show();*/

        btn =(Button)findViewById(R.id.btn2);
        btn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View V)
            {

                /*Intent intent = new Intent();
                intent.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(intent);*/
                testThumbnails();




            }

        });




    }

    public  void testThumbnails(){

        String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        String path2="content://downloads/all_downloads/";
        String imagePath = path + "abc.jpg";

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
        File file = new File(Environment.getExternalStorageDirectory() + "ooo" + ".jpg");

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
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setMessage("nihao").show();
        ImageView myImage=null;
        myImage=(ImageView)findViewById(R.id.image);
        myImage.setImageBitmap(bmap);


    }


    public Bitmap getImageThumbnail(String imagePath,int width,int height)
    {
        Bitmap bmap=null;
        BitmapFactory.Options opt=new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        bmap=BitmapFactory.decodeFile(imagePath,opt);
        opt.inJustDecodeBounds=false;

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
        bmap=ThumbnailUtils.extractThumbnail(bmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bmap;

    }

    public void saveMyBitmap(String bitName) throws IOException {

        String path = Environment.getExternalStorageDirectory() + "/";
        String name1 = path + "DCIM/Camera/adc.jpg";
        File originalFile = new File(name1);




        Bitmap bmp = decodeFile(originalFile);
        File f = new File(path + bitName + ".jpg");
        f.createNewFile();

        FileOutputStream fOut = null;

        try {

            fOut = new FileOutputStream(f);



        if(bmp.compress(Bitmap.CompressFormat.PNG, 30, fOut)){
            fOut.flush();
            fOut.close();
        }



        } catch (FileNotFoundException e) {

            e.printStackTrace();




        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private Bitmap decodeFile(File f){

        try {

            //Decode image size

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to

            final int REQUIRED_HEIGHT=250;
            final int REQUIRED_WIDTH=150;


            //Find the correct scale value. It should be the power of 2.

            int width_tmp=o.outWidth, height_tmp=o.outHeight;


            AlertDialog.Builder build = new AlertDialog.Builder(this);

            build.setMessage("nihao").show();
            System.out.println(width_tmp+"  "+height_tmp);

            Log.w("===", (width_tmp+"  "+height_tmp));

            int scale=1;

            while(true){

                if(width_tmp/2<REQUIRED_WIDTH && height_tmp/2<REQUIRED_HEIGHT)

                    break;

                width_tmp/=2;

                height_tmp/=2;

                scale++;



                Log.w("===", scale+"''"+width_tmp+"  "+height_tmp);

            }


            //Decode with inSampleSize

            BitmapFactory.Options o2 = new BitmapFactory.Options();

            o2.inSampleSize=scale;

            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

        } catch (FileNotFoundException e) {}

        return null;

    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
