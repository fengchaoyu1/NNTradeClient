package zhaohg.main;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import zhaohg.account.LoginActivity;
import zhaohg.account.RegisterActivity;
import zhaohg.api.account.AccountLogin;
import zhaohg.testable.TestableLinearLayout;

public class MainDrawer extends TestableLinearLayout {

    private Context context;
    private View view;

    public MainDrawer(Context context) {
        super(context);
        this.initView(context);
    }

    public MainDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public MainDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.main_drawer, null);
        this.addView(view);

        Button registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnRegisterButtonClickListener());
        Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnLoginButtonClickListener());

        this.updateUserLayout();
    }

    public void updateUserLayout() {
        Button registerButton = (Button) view.findViewById(R.id.register_button);
        Button loginButton = (Button) view.findViewById(R.id.login_button);
        ImageView portraitView = (ImageView) view.findViewById(R.id.image_portrait);
        TextView usernameText = (TextView) view.findViewById(R.id.text_username);
        AccountLogin login = new AccountLogin(context);
        if (login.hasLogin()) {
            if (registerButton.getVisibility() != GONE) {
                registerButton.setVisibility(GONE);
                loginButton.setVisibility(GONE);
                portraitView.setVisibility(VISIBLE);
                usernameText.setVisibility(VISIBLE);
                usernameText.setText(login.loadUsername());
            }
        } else {
            if (registerButton.getVisibility() != VISIBLE) {
                registerButton.setVisibility(VISIBLE);
                loginButton.setVisibility(VISIBLE);
                portraitView.setVisibility(GONE);
                usernameText.setVisibility(GONE);
            }
        }
    }

    private class OnRegisterButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        }
    }

    private class OnLoginButtonClickListener implements  OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
