package zhaohg.test.account;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import zhaohg.account.RegisterActivity;
import zhaohg.api.Encryption;
import zhaohg.trade.OnTestFinishedListener;
import zhaohg.trade.R;

public class TestRegisterActivity extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private RegisterActivity activity;

    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirm;
    private Button registerButton;
    private TextView textError;

    private String existedUsername;
    private String existedPasword;

    public TestRegisterActivity(Class<RegisterActivity> activityClass) {
        super(activityClass);
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
        this.editUsername.setText(generateRandomName());
        this.editPassword.setText("register_activity");
        this.editConfirm.setText("register_activity");
        this.activity.setOnTestFinishedListener(new OnTestFinishedListener() {
            @Override
            public void onTaskFinished() {
                signal.countDown();
            }
        });
        this.registerButton.performClick();
        signal.await();
        assertEquals(View.GONE, this.textError.getVisibility());
    }

    public void testEmptyNameField() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        this.editPassword.setText("register_activity");
        this.editConfirm.setText("register_activity");
        this.activity.setOnTestFinishedListener(new OnTestFinishedListener() {
            @Override
            public void onTaskFinished() {
                signal.countDown();
            }
        });
        this.registerButton.performClick();
        signal.await();
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testEmptyPasswordField() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        this.editUsername.setText(generateRandomName());
        this.editConfirm.setText("register_activity");
        this.activity.setOnTestFinishedListener(new OnTestFinishedListener() {
            @Override
            public void onTaskFinished() {
                signal.countDown();
            }
        });
        this.registerButton.performClick();
        signal.await();
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_field_required), this.textError.getText());
    }

    public void testMismatchPassword() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        this.editUsername.setText(generateRandomName());
        this.editPassword.setText("register_activity_1");
        this.editConfirm.setText("register_activity_2");
        this.activity.setOnTestFinishedListener(new OnTestFinishedListener() {
            @Override
            public void onTaskFinished() {
                signal.countDown();
            }
        });
        this.registerButton.performClick();
        signal.await();
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(this.activity.getString(R.string.error_invalid_confirm), this.textError.getText());
    }

}
