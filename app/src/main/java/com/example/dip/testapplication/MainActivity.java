package com.example.dip.testapplication;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
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

import java.io.ByteArrayOutputStream;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class MainActivity extends ActionBarActivity {
    Button btn =null;
    ImageView myView=null;
    ImageView myView3=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        //StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
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

        Bitmap mBitmap;
        int[] mColors;
        int WIDTH=500;
        int HEIGHT=500;
        int STRIDE=640;//must be >=WIDTH

        mColors=initColors(STRIDE,HEIGHT,WIDTH);
        mBitmap=Bitmap.createBitmap(mColors, 0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        //mJpegs=codec(mBitmap,Bitmap.CompressFormat.JPEG,80);
        ImageView myImage2=null;
        myImage2=(ImageView)findViewById(R.id.myView);
        myImage2.setImageBitmap(mBitmap);



        File file1 = new File(Environment.getExternalStorageDirectory() + "/","abc" + ".jpg");
        try{
            FileOutputStream out =new FileOutputStream(file1);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setMessage("按HELLO World 缩略").show();








        /*AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setMessage("nihao").show();*/
        myView3=(ImageView)findViewById(R.id.imageInternet);


        btn =(Button)findViewById(R.id.btn2);
        btn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View V)
            {

                /*Intent intent = new Intent();
                intent.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(intent);*/
                String imagePath="http://ts2.mm.bing.net/th?id=HN.608007102913186522&pid=1.7";
                testThumbnails();
                RequestTask requestTask=new RequestTask();
                requestTask.execute(imagePath);
                /*
                ImageView myImage3=null;
                myImage3=(ImageView)findViewById(R.id.imageInternet);
                myImage3.setImageBitmap(bitmap);*/




            }

        });




    }


    public   Bitmap codec(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
             ByteArrayOutputStream baos=new ByteArrayOutputStream();
             bitmap.compress(format, quality, baos);
             byte[] data=baos.toByteArray();
             return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public int[] initColors(int STRIDE,int HEIGHT,int WIDTH) {
           int[] colors=new int[STRIDE*HEIGHT];
           for (int y = 0; y < HEIGHT; y++) {//use of x,y is legible then the use of i,j
                for (int x = 0; x < WIDTH; x++) {
                     int r = x * 255 / (WIDTH - 1);
                     int g = y * 255 / (HEIGHT - 1);
                     int b = 255 - Math.min(r, g);
                     int a = Math.max(r, g);
                     colors[y*STRIDE+x]=(a<<24)|(r<<16)|(g<<8)|(b);//the shift operation generates the color ARGB
                }
           }
           return colors;
    }





    public  void testThumbnails(){

        String path = Environment.getExternalStorageDirectory() + "/";
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

        int compressSize=1;
        if (beWidth < beHeight) {
            compressSize = beWidth;
        } else {
            compressSize = beHeight;
        }
        if (compressSize <= 0) {
            compressSize = 1;
        }
        opt.inSampleSize=compressSize;
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

        build.setMessage("缩略完成").show();
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
    private class RequestTask extends AsyncTask<String, Integer, Bitmap> {


        protected Bitmap doInBackground(String ...imagePath) {

            HttpGet httpRequest =new HttpGet(imagePath[0]);
            HttpClient httpClient=new DefaultHttpClient();
            Bitmap bitmap=null;
            try{
                HttpResponse httpResponse=httpClient.execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    HttpEntity httpEntity=httpResponse.getEntity();
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

            myView3.setImageBitmap(bitmap);
        }

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

