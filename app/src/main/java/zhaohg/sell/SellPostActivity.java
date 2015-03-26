package zhaohg.sell;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import zhaohg.api.sell.GetSellPost;
import zhaohg.api.sell.GetSellPostPostEvent;
import zhaohg.api.sell.SellPost;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class SellPostActivity extends TestableActionBarActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";

    private String postId = "";

    private TextView textTitle;
    private TextView textDescription;
    private TextView textError;

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
        GetSellPost getSellPost = new GetSellPost(context);
        getSellPost.setParameter(this.postId);
        getSellPost.setEvent(new GetSellPostPostEvent() {
            @Override
            public void onSuccess(SellPost post) {
                textTitle.setVisibility(View.VISIBLE);
                textDescription.setVisibility(View.VISIBLE);
                textTitle.setText(post.getTitle());
                textDescription.setText(post.getDescription());
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

}
