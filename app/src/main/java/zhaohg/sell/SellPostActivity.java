package zhaohg.sell;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zhaohg.api.sell.DeleteSellPost;
import zhaohg.api.sell.DeleteSellPostPostEvent;
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
    private boolean isOwner = false;

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
                Intent intent = new Intent(context, EditSellPostActivity.class);
                intent.putExtra(EditSellPostActivity.EXTRA_POST_ID, postId);
                intent.putExtra(EditSellPostActivity.EXTRA_TITLE, this.textTitle.getText().toString());
                intent.putExtra(EditSellPostActivity.EXTRA_DESCRIPTION, this.textDescription.getText().toString());
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
                        DeleteSellPost deleteSellPost = new DeleteSellPost(context);
                        deleteSellPost.setParameter(postId);
                        deleteSellPost.setEvent(new DeleteSellPostPostEvent() {
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
                        deleteSellPost.request();
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
                isOwner = post.getUserId().equals(getSellPost.loadUserId());
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
