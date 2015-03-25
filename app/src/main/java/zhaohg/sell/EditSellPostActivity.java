package zhaohg.sell;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import zhaohg.api.ApiErrno;
import zhaohg.api.sell.NewSellPost;
import zhaohg.api.sell.NewSellPostPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class EditSellPostActivity extends TestableActionBarActivity {

    private EditText editTitle;
    private EditText editDescription;
    private Button postButton;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sell_post);

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
            String title = editTitle.getText().toString();
            final Context context = v.getContext();
            String description = editDescription.getText().toString();
            if (title.isEmpty() || description.isEmpty()) {
                showErrorMessage(context.getString(R.string.error_field_required));
                finishTest();
                return;
            }
            postButton.setEnabled(false);
            NewSellPost newSellPost = new NewSellPost(v.getContext());
            newSellPost.setParameter(title, description, new ArrayList<String>());
            newSellPost.setEvent(new NewSellPostPostEvent() {
                @Override
                public void onSuccess(String postId) {
                    // TODO: Goto new post.
                    finishTest();
                }
                @Override
                public void onFailure(int errno) {
                    postButton.setEnabled(true);
                    showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                    finishTest();
                }
            });
            newSellPost.request();
        }
    }

}
