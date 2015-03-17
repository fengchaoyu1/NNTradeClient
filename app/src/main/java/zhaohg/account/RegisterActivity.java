package zhaohg.account;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import zhaohg.api.ApiErrno;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.trade.R;

public class RegisterActivity extends Activity {

    private ProgressBar progressBar;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirm;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.progressBar = (ProgressBar) this.findViewById(R.id.register_progress);
        this.editUsername = (EditText) this.findViewById(R.id.username);
        this.editPassword = (EditText) this.findViewById(R.id.password);
        this.editConfirm = (EditText) this.findViewById(R.id.confirm);
        this.registerButton = (Button) this.findViewById(R.id.register_button);

        this.registerButton.setOnClickListener(new OnRegisterClick());
    }

    private void showToastMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private class OnRegisterClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final Context context = getApplicationContext();
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                showToastMessage(context.getString(R.string.error_field_required));
                return;
            }
            String confirm = editConfirm.getText().toString();
            if (password != confirm) {
                showToastMessage(context.getString(R.string.error_invalid_confirm));
                return;
            }
            AccountRegister register = new AccountRegister(context);
            register.setParameter(username, password);
            register.setEvent(new AccountRegisterPostEvent() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    showToastMessage(context.getString(R.string.action_register_complete));
                    onBackPressed();
                }

                @Override
                public void onFailure(int errno) {
                    progressBar.setVisibility(View.GONE);
                    showToastMessage(ApiErrno.getErrorMessage(context, errno));
                }
            });
            progressBar.setVisibility(View.VISIBLE);
            register.request();
        }
    }
}



