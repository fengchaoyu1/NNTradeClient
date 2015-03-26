package zhaohg.test.sell;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.Encryption;
import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.main.R;
import zhaohg.sell.EditSellPostActivity;
import zhaohg.test.helper.RandomName;
import zhaohg.test.helper.RegisterAndLogin;
import zhaohg.testable.OnTestFinishedListener;

public class TestNewPost extends ActivityInstrumentationTestCase2<EditSellPostActivity> {

    private EditSellPostActivity activity;

    private EditText editTitle;
    private EditText editDescription;
    private Button postButton;
    private TextView textError;

    public TestNewPost() {
        super(EditSellPostActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.editTitle = (EditText) this.activity.findViewById(R.id.edit_title);
        this.editDescription = (EditText) this.activity.findViewById(R.id.edit_description);
        this.textError = (TextView) this.activity.findViewById(R.id.text_error);
        this.postButton = (Button) this.activity.findViewById(R.id.button_post);

        Context context = this.activity.getApplicationContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_new_post_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    public void testNewPostNormal() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTitle.setText("New Post in Activity");
                editDescription.setText("New Description");
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal.countDown();
                    }
                });
                postButton.performClick();
            }
        });
        signal.await();
        assertFalse(this.postButton.isEnabled());
        assertEquals(View.GONE, this.textError.getVisibility());
    }

    public void testNewPostMissTitle() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTitle.setText("");
                editDescription.setText("New Description");
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal.countDown();
                    }
                });
                postButton.performClick();
            }
        });
        signal.await();
        assertTrue(this.postButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testNewPostMissDescription() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTitle.setText("New Post in Activity");
                editDescription.setText("");
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal.countDown();
                    }
                });
                postButton.performClick();
            }
        });
        signal.await();
        assertTrue(this.postButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

}
