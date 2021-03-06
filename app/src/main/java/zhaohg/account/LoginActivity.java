package zhaohg.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import zhaohg.api.ApiErrno;
import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class LoginActivity extends TestableActionBarActivity {

    public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";

    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private Button registerButton;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.editUsername = (EditText) this.findViewById(R.id.username);
        this.editPassword = (EditText) this.findViewById(R.id.password);
        this.loginButton = (Button) this.findViewById(R.id.login_button);
        this.registerButton = (Button) this.findViewById(R.id.register_button);
        this.textError = (TextView) this.findViewById(R.id.text_error);

        this.loginButton.setOnClickListener(new OnLoginClick());
        this.registerButton.setOnClickListener(new OnRegisterClick());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String username = extras.getString(EXTRA_USERNAME, "");
            String password = extras.getString(EXTRA_PASSWORD, "");
            this.editUsername.setText(username);
            this.editPassword.setText(password);
            if (!username.isEmpty() && !password.isEmpty()) {
                this.loginButton.performClick();
            }
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

    private class OnLoginClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final Context context = getApplicationContext();
            hideErrorMessage();
            final String username = editUsername.getText().toString();
            final String password = editPassword.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                showErrorMessage(context.getString(R.string.error_field_required));
                finishTest();
                return;
            }
            AccountLogin login = new AccountLogin(context);
            login.setParameter(username, password);
            login.setEvent(new AccountLoginPostEvent() {
                @Override
                public void onSuccess() {
                    finishTest();
                    finish();
                }

                @Override
                public void onFailure(int errno) {
                    loginButton.setEnabled(true);
                    showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                    finishTest();
                }
            });
            loginButton.setEnabled(false);
            login.request();
        }
    }

    private class OnRegisterClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            finish();
        }
    }

}
