package zhaohg.test.account;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;

import zhaohg.account.LoginActivity;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.test.helper.RandomName;
import zhaohg.testable.OnTestFinishedListener;
import zhaohg.main.R;

public class TestLogin extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity activity;

    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private TextView textError;

    public TestLogin() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();

        this.editUsername = (EditText) this.activity.findViewById(R.id.username);
        this.editPassword = (EditText) this.activity.findViewById(R.id.password);
        this.loginButton = (Button) this.activity.findViewById(R.id.login_button);
        this.textError = (TextView) this.activity.findViewById(R.id.text_error);
    }

    public void testLoginNormal() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = RandomName.generateRandomName("test_login_normal");
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(existedUsername);
                        editPassword.setText(existedPassword);
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        loginButton.performClick();
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
        assertFalse(this.loginButton.isEnabled());
        assertEquals(View.GONE, this.textError.getVisibility());
    }

    public void testLoginEmptyName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = RandomName.generateRandomName("test_login_empty_name");
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText("");
                        editPassword.setText(existedPassword);
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        loginButton.performClick();
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
        assertTrue(this.loginButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testLoginEmptyPassword() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = RandomName.generateRandomName("test_login_empty_password");
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(existedUsername);
                        editPassword.setText("");
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        loginButton.performClick();
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
        assertTrue(this.loginButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testLoginNonExistName() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = RandomName.generateRandomName("test_login_non_existed");
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(existedUsername + "_non");
                        editPassword.setText(existedPassword);
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        loginButton.performClick();
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
        assertTrue(this.loginButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.errno_username_non_exist), this.textError.getText());
    }

    public void testLoginWrongPassword() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AccountRegister register = new AccountRegister(this.activity.getApplicationContext());
        final String existedUsername = RandomName.generateRandomName("test_login_wrong_password");
        final String existedPassword = "existed";
        register.setParameter(existedUsername, existedPassword);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editUsername.setText(existedUsername);
                        editPassword.setText(existedPassword + "_non");
                        activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                            @Override
                            public void onTaskFinished() {
                                signal.countDown();
                            }
                        });
                        loginButton.performClick();
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
        assertTrue(this.loginButton.isEnabled());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.errno_mismatch_username_password), this.textError.getText());
    }

}
