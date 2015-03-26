package zhaohg.sell;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zhaohg.api.sell.GetSellPost;
import zhaohg.api.sell.GetSellPostPostEvent;
import zhaohg.api.sell.SellPost;
import zhaohg.api.sell.UpdateSellPost;
import zhaohg.api.sell.UpdateSellPostPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class SellPostActivity extends TestableActionBarActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";

    private String postId = "";

    private TextView textTitle;
    private TextView textDescription;
    private TextView textDate;
    private Switch switchOpen;

    private TextView textError;

    private SellPost sellPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.textTitle = (TextView) findViewById(R.id.text_title);
        this.textDescription = (TextView) findViewById(R.id.text_description);
        this.textDate = (TextView) findViewById(R.id.text_date);
        this.switchOpen = (Switch) findViewById(R.id.switch_open);
        this.switchOpen.setOnCheckedChangeListener(new OnOpenCheckedChangeListener());

        this.textError = (TextView) findViewById(R.id.text_error);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.postId = extras.getString(EXTRA_POST_ID, "");
        }

        this.loadInfo();
    }

    private void hideErrorMessage() {
        this.textError.setVisibility(View.GONE);
        this.textError.setText("");
    }

    private void showErrorMessage(String text) {
        this.textError.setVisibility(View.VISIBLE);
        this.textError.setText(text);
    }

    public void loadInfo() {
        final Context context = this.getApplicationContext();
        final GetSellPost getSellPost = new GetSellPost(context);
        getSellPost.setParameter(this.postId);
        getSellPost.setEvent(new GetSellPostPostEvent() {
            @Override
            public void onSuccess(SellPost post) {
                sellPost = post;
                textTitle.setText(post.getTitle());
                textDescription.setText(post.getDescription());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                textDate.setText(context.getString(R.string.publish_date_) + dateFormat.format(post.getPostDate()));
                switchOpen.setChecked(post.isOpen());
                if (post.getUserId() != getSellPost.loadUserId()) {
                    switchOpen.setEnabled(false);
                }
                hideErrorMessage();
                finishTest();
            }

            @Override
            public void onFailure(int errno) {
                showErrorMessage(context.getString(R.string.error_post_not_exist));
                finishTest();
            }
        });
        getSellPost.request();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    private class OnOpenCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Context context = buttonView.getContext();
            if (sellPost.isOpen() != isChecked) {
                UpdateSellPost updateSellPost = new UpdateSellPost(context);
                updateSellPost.setParameter(postId);
                updateSellPost.setUpdateOpen(isChecked);
                updateSellPost.setEvent(new UpdateSellPostPostEvent() {
                    @Override
                    public void onSuccess() {
                        finishTest();
                    }
                    @Override
                    public void onFailure(int errno) {
                        finishTest();
                    }
                });
                updateSellPost.request();
                sellPost.setOpen(isChecked);
            }
            if (isChecked) {
                switchOpen.setText(context.getString(R.string.sell_open));
            } else {
                switchOpen.setText(context.getString(R.string.sell_close));
            }
        }
    }
}
