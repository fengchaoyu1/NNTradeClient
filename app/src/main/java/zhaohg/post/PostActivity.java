package zhaohg.post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import zhaohg.api.post.DeletePost;
import zhaohg.api.post.DeletePostPostEvent;
import zhaohg.api.post.GetPost;
import zhaohg.api.post.GetPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.api.post.UpdatePost;
import zhaohg.api.post.UpdatePostPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class PostActivity extends TestableActionBarActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";

    private String postId = "";

    private TextView textUsername;
    private TextView textTitle;
    private TextView textDescription;
    private TextView textPublishDate;
    private TextView textModifyDate;
    private Switch switchOpen;

    private LinearLayoutManager layoutManager;
    private RecyclerView recycleComments;

    private EditText editMessage;
    private ImageView buttonSend;

    private TextView textError;

    private Post post;
    private boolean isOwner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.textUsername = (TextView) findViewById(R.id.text_username);
        this.textTitle = (TextView) findViewById(R.id.text_title);
        this.textDescription = (TextView) findViewById(R.id.text_description);
        this.textPublishDate = (TextView) findViewById(R.id.text_publish_date);
        this.textModifyDate = (TextView) findViewById(R.id.text_modify_date);
        this.switchOpen = (Switch) findViewById(R.id.switch_open);
        this.switchOpen.setOnCheckedChangeListener(new OnOpenCheckedChangeListener());

        this.recycleComments = (RecyclerView) findViewById(R.id.recycle_comments);
        this.layoutManager = new LinearLayoutManager(this);
        this.recycleComments.setLayoutManager(layoutManager);

        this.editMessage = (EditText) findViewById(R.id.edit_message);
        this.buttonSend = (ImageView) findViewById(R.id.button_send);

        this.textError = (TextView) findViewById(R.id.text_error);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.postId = extras.getString(EXTRA_POST_ID, "");
        }

        this.loadInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sell_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionEdit = menu.findItem(R.id.action_edit);
        MenuItem actionDelete = menu.findItem(R.id.action_delete);
        if (this.isOwner) {
            actionEdit.setVisible(true);
            actionDelete.setVisible(true);
        } else {
            actionEdit.setVisible(false);
            actionDelete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context context = this.getApplicationContext();
        switch(item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.putExtra(EditPostActivity.EXTRA_POST_ID, postId);
                intent.putExtra(EditPostActivity.EXTRA_TITLE, this.textTitle.getText().toString());
                intent.putExtra(EditPostActivity.EXTRA_DESCRIPTION, this.textDescription.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(context.getString(R.string.sell_delete));
                builder.setMessage(context.getString(R.string.sure_to_delete));
                builder.setPositiveButton(context.getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeletePost deletePost = new DeletePost(context);
                        deletePost.setParameter(postId);
                        deletePost.setEvent(new DeletePostPostEvent() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(), context.getString(R.string.sell_delete_success), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            @Override
                            public void onFailure(int errno) {
                                Toast.makeText(getApplicationContext(), context.getString(R.string.sell_delete_fail), Toast.LENGTH_SHORT).show();
                            }
                        });
                        deletePost.request();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(context.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        final GetPost getPost = new GetPost(context);
        getPost.setParameter(this.postId);
        getPost.setEvent(new GetPostPostEvent() {
            @Override
            public void onSuccess(Post post) {
                PostActivity.this.post = post;
                textUsername.setText(post.getUserName());
                textTitle.setText(post.getTitle());
                textDescription.setText(post.getDescription());
                textPublishDate.setText(context.getString(R.string.publish_date_) + post.getPostDateString());
                textModifyDate.setText(context.getString(R.string.modify_date_) + post.getModifyDateString());
                switchOpen.setChecked(post.isOpen());
                isOwner = post.getUserId().equals(getPost.loadUserId());
                switchOpen.setEnabled(isOwner);
                invalidateOptionsMenu();
                hideErrorMessage();
                finishTest();
            }

            @Override
            public void onFailure(int errno) {
                showErrorMessage(context.getString(R.string.error_post_not_exist));
                finishTest();
            }
        });
        getPost.request();
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
            if (post.isOpen() != isChecked) {
                UpdatePost updatePost = new UpdatePost(context);
                updatePost.setParameter(postId);
                updatePost.setUpdateOpen(isChecked);
                updatePost.setEvent(new UpdatePostPostEvent() {
                    @Override
                    public void onSuccess() {
                        finishTest();
                    }
                    @Override
                    public void onFailure(int errno) {
                        finishTest();
                    }
                });
                updatePost.request();
                post.setOpen(isChecked);
            }
            if (isChecked) {
                switchOpen.setText(context.getString(R.string.sell_open));
            } else {
                switchOpen.setText(context.getString(R.string.sell_close));
            }
        }
    }
}
