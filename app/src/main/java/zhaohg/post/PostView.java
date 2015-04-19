package zhaohg.post;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import zhaohg.main.R;

public class PostView extends LinearLayout {
    public PostView(Context context) {
        super(context);
        this.initView();
    }
    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }public PostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }
    public void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_upload_image, this, false);
        this.addView(view);
    }
}

