package zhaohg.account;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import zhaohg.api.ApiErrno;
import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActivity;

public class LoginActivity extends TestableActivity {

    private ProgressBar progressBar;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.progressBar = (ProgressBar) this.findViewById(R.id.login_progress);
        this.editUsername = (EditText) this.findViewById(R.id.username);
        this.editPassword = (EditText) this.findViewById(R.id.password);
        this.loginButton = (Button) this.findViewById(R.id.login_button);
        this.textError = (TextView) this.findViewById(R.id.text_error);

        this.loginButton.setOnClickListener(new OnLoginClick());
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
                    progressBar.setVisibility(View.GONE);
                    finishTest();
                }

                @Override
                public void onFailure(int errno) {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                    finishTest();
                }
            });
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            login.request();
        }
    }

}
