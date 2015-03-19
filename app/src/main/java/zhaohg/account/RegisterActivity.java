package zhaohg.account;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import zhaohg.api.ApiErrno;
import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.trade.R;
import zhaohg.trade.TestableActivity;

public class RegisterActivity extends TestableActivity {

    private ProgressBar progressBar;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirm;
    private Button registerButton;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.progressBar = (ProgressBar) this.findViewById(R.id.register_progress);
        this.editUsername = (EditText) this.findViewById(R.id.username);
        this.editPassword = (EditText) this.findViewById(R.id.password);
        this.editConfirm = (EditText) this.findViewById(R.id.confirm);
        this.registerButton = (Button) this.findViewById(R.id.register_button);
        this.textError = (TextView) this.findViewById(R.id.text_error);

        this.registerButton.setOnClickListener(new OnRegisterClick());
    }

    private void hideErrorMessage() {
        this.textError.setVisibility(View.GONE);
        this.textError.setText("");
    }

    private void showErrorMessage(String text) {
        this.textError.setVisibility(View.VISIBLE);
        this.textError.setText(text);
    }

    private class OnRegisterClick implements View.OnClickListener {
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
            String confirm = editConfirm.getText().toString();
            if (!password.equals(confirm)) {
                showErrorMessage(context.getString(R.string.error_invalid_confirm));
                finishTest();
                return;
            }
            AccountRegister register = new AccountRegister(context);
            register.setParameter(username, password);
            register.setEvent(new AccountRegisterPostEvent() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    finishTest();
                }

                @Override
                public void onFailure(int errno) {
                    progressBar.setVisibility(View.GONE);
                    registerButton.setEnabled(true);
                    showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                    finishTest();
                }
            });
            progressBar.setVisibility(View.VISIBLE);
            registerButton.setEnabled(false);
            register.request();
        }
    }

}



