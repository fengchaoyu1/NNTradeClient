package com.example.dip.testapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Dip on 2015/3/28.
 */
public class CustomView extends LinearLayout{
    public CustomView(Context context){
        super(context);
    }
    private LinearLayout linearLayout;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private List<ImageView> imageViewList;
    public void Init(){
        linearLayout.setOrientation(HORIZONTAL);
        imageView1.setImageResource(R.drawable.darkstar);
        imageViewList.add(imageView1);

        imageView2.setImageResource(R.drawable.darkstar);
        imageViewList.add(imageView2);

        imageView3.setImageResource(R.drawable.darkstar);
        imageViewList.add(imageView3);

        imageView4.setImageResource(R.drawable.darkstar);
        imageViewList.add(imageView4);

        imageView5.setImageResource(R.drawable.darkstar);
        imageViewList.add(imageView5);

        linearLayout.addView(imageView1);
        linearLayout.addView(imageView2);
        linearLayout.addView(imageView3);
        linearLayout.addView(imageView4);
        linearLayout.addView(imageView5);




    }

}
