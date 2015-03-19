package zhaohg.test.account;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import zhaohg.account.RegisterActivity;
import zhaohg.api.Encryption;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.trade.OnTestFinishedListener;
import zhaohg.trade.R;

public class TestRegisterActivity extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private RegisterActivity activity;

    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirm;
    private Button registerButton;
    private TextView textError;

    public TestRegisterActivity() {
        super(RegisterActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();

        this.editUsername = (EditText) this.activity.findViewById(R.id.username);
        this.editPassword = (EditText) this.activity.findViewById(R.id.password);
        this.editConfirm = (EditText) this.activity.findViewById(R.id.confirm);
        this.registerButton = (Button) this.activity.findViewById(R.id.register_button);
        this.textError = (TextView) this.activity.findViewById(R.id.text_error);
    }

    private String generateRandomName() {
        Calendar calendar = Calendar.getInstance();
        String text = "register_activity_" + calendar.getTimeInMillis();
        return "register_activity_" + Encryption.md5(text);
    }

    public void testNormal() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(generateRandomName());
                        editPassword.setText("register_activity");
                        editConfirm.setText("register_activity");
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        registerButton.performClick();
                    }
                }
        );
        signal.await();
        assertFalse(this.registerButton.isEnabled());
        assertEquals(View.GONE, this.textError.getVisibility());
    }

    public void testEmptyNameField() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        editPassword.setText("register_activity");
                        editConfirm.setText("register_activity");
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        registerButton.performClick();
                    }
                }
        );
        signal.await();
        assertTrue(this.registerButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testEmptyPasswordField() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(generateRandomName());
                        editConfirm.setText("register_activity");
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        registerButton.performClick();
                    }
                }
        );
        signal.await();
        assertTrue(this.registerButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testMismatchPassword() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(generateRandomName());
                        editPassword.setText("register_activity_1");
                        editConfirm.setText("register_activity_2");
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        registerButton.performClick();
                    }
                }
        );
        signal.await();
        assertTrue(this.registerButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_invalid_confirm), this.textError.getText());
    }

    public void testExistedUsername() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = this.generateRandomName();
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                editUsername.setText(existedUsername);
                                editPassword.setText(existedPassword);
                                editConfirm.setText(existedPassword);
                                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                                    @Override
                                    public void onTaskFinished() {
                                        signal.countDown();
                                    }
                                });
                                registerButton.performClick();
                            }
                        }
                );
            }
            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        register.request();
        signal.await();
        assertTrue(this.registerButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.errno_username_exist), this.textError.getText());
    }

}
