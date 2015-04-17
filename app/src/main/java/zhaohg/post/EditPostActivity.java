package zhaohg.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import zhaohg.api.ApiErrno;
import zhaohg.api.post.NewPost;
import zhaohg.api.post.NewPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.api.post.UpdatePost;
import zhaohg.api.post.UpdatePostPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class EditPostActivity extends TestableActionBarActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";
    public static final String EXTRA_POST_TYPE = "EXTRA_POST_TYPE";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";

    private EditText editTitle;
    private EditText editDescription;
    private Button postButton;
    private TextView textError;

    private String postId = "";
    private int postType = Post.POST_TYPE_SELL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.editTitle = (EditText) findViewById(R.id.edit_title);
        this.editDescription = (EditText) findViewById(R.id.edit_description);
        this.textError = (TextView) findViewById(R.id.text_error);

        this.postButton = (Button) findViewById(R.id.button_post);
        this.postButton.setOnClickListener(new OnPostButtonClickListener());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.postId = extras.getString(EXTRA_POST_ID, "");
            this.postType = extras.getInt(EXTRA_POST_TYPE, Post.POST_TYPE_SELL);
            this.editTitle.setText(extras.getString(EXTRA_TITLE, ""));
            this.editDescription.setText(extras.getString(EXTRA_DESCRIPTION, ""));
        }
    }

    private void hideErrorMessage() {
        this.textError.setVisibility(View.GONE);
        this.textError.setText("");
    }

    private void showErrorMessage(String text) {
        this.textError.setVisibility(View.VISIBLE);
        this.textError.setText(text);
    }

    private class OnPostButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            hideErrorMessage();
            final Context context = v.getContext();
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            if (title.isEmpty() || description.isEmpty()) {
                showErrorMessage(context.getString(R.string.error_field_required));
                finishTest();
                return;
            }
            postButton.setEnabled(false);
            if (postId.isEmpty()) {
                // Create new post.
                NewPost newPost = new NewPost(context);
                newPost.setParameter(postType, title, description, new ArrayList<String>());
                newPost.setEvent(new NewPostPostEvent() {
                    @Override
                    public void onSuccess(String postId) {
                        Intent intent = new Intent(context, PostActivity.class);
                        intent.putExtra(PostActivity.EXTRA_POST_ID, postId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finishTest();
                        finish();
                    }
                    @Override
                    public void onFailure(int errno) {
                        postButton.setEnabled(true);
                        showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                        finishTest();
                    }
                });
                newPost.request();
            } else {
                // This is an update.
                UpdatePost updatePost = new UpdatePost(context);
                updatePost.setParameter(postId);
                updatePost.setUpdateTitle(editTitle.getText().toString());
                updatePost.setUpdateDescription(editDescription.getText().toString());
                updatePost.setEvent(new UpdatePostPostEvent() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(context, PostActivity.class);
                        intent.putExtra(PostActivity.EXTRA_POST_ID, postId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finishTest();
                        finish();
                    }
                    @Override
                    public void onFailure(int errno) {
                        postButton.setEnabled(true);
                        showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                        finishTest();
                    }
                });
                updatePost.request();
            }
        }
    }

}
