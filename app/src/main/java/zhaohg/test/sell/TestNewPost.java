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
import zhaohg.testable.OnTestFinishedListener;

public class TestNewPost extends ActivityInstrumentationTestCase2<EditSellPostActivity> {

    private EditSellPostActivity activity;

    private EditText editTitle;
    private EditText editDescription;
    private Button postButton;
    private TextView textError;

    private int localErrno;

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

        this.registerAndLogin();
    }

    private String generateRandomName() {
        Calendar calendar = Calendar.getInstance();
        String text = "new_post_" + calendar.getTimeInMillis();
        return "new_post_" + Encryption.md5(text);
    }

    private void registerAndLogin() throws Exception {
        final String username = generateRandomName();
        final String password = "password";
        final Context context = this.activity.getApplicationContext();
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(context);
        register.setParameter(username, password);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                AccountLogin login = new AccountLogin(context);
                login.setParameter(username, password);
                login.setEvent(new AccountLoginPostEvent() {
                    @Override
                    public void onSuccess() {
                        signal.countDown();
                    }
                    @Override
                    public void onFailure(int errno) {
                        localErrno = errno;
                        signal.countDown();
                    }
                });
                login.request();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        register.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testNewPostNormal() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = this.generateRandomName();
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
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
            }
            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        register.request();
        signal.await();
        assertFalse(this.postButton.isEnabled());
        assertEquals(View.GONE, this.textError.getVisibility());
    }

    public void testNewPostMissTitle() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = this.generateRandomName();
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
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
            }
            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        register.request();
        signal.await();
        assertTrue(this.postButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testNewPostMissDescription() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = this.generateRandomName();
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
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
            }
            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        register.request();
        signal.await();
        assertTrue(this.postButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

}
