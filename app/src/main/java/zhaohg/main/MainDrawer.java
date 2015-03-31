package zhaohg.main;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zhaohg.account.LoginActivity;
import zhaohg.api.account.AccountLogin;
import zhaohg.testable.TestableLinearLayout;

public class MainDrawer extends TestableLinearLayout {

    public static final int ITEM_SELL = 0;
    public static final int ITEM_BUY = 1;
    public static final int ITEM_CHAT = 2;

    public interface OnSelectedItemChanged {
        public void onSelectedItemChanged(int id);
    }

    private Context context;
    private View view;
    private OnSelectedItemChanged onSelectedItemChanged;

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

        Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnLoginButtonClickListener());

        this.updateUserLayout();

        LinearLayout layoutSell = (LinearLayout) view.findViewById(R.id.layout_sell);
        layoutSell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectedItemChanged != null) {
                    onSelectedItemChanged.onSelectedItemChanged(ITEM_SELL);
                }
            }
        });
        LinearLayout layoutBuy = (LinearLayout) view.findViewById(R.id.layout_buy);
        layoutBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectedItemChanged != null) {
                    onSelectedItemChanged.onSelectedItemChanged(ITEM_BUY);
                }
            }
        });
        LinearLayout layoutChat = (LinearLayout) view.findViewById(R.id.layout_chat);
        layoutChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectedItemChanged != null) {
                    onSelectedItemChanged.onSelectedItemChanged(ITEM_CHAT);
                }
            }
        });
    }

    public void updateUserLayout() {
        Button loginButton = (Button) view.findViewById(R.id.login_button);
        ImageView portraitView = (ImageView) view.findViewById(R.id.image_portrait);
        TextView usernameText = (TextView) view.findViewById(R.id.text_username);
        AccountLogin login = new AccountLogin(context);
        if (login.hasLogin()) {
            if (loginButton.getVisibility() != GONE) {
                loginButton.setVisibility(GONE);
                portraitView.setVisibility(VISIBLE);
                usernameText.setVisibility(VISIBLE);
                usernameText.setText(login.loadUsername());
            }
        } else {
            if (loginButton.getVisibility() != VISIBLE) {
                loginButton.setVisibility(VISIBLE);
                portraitView.setVisibility(GONE);
                usernameText.setVisibility(GONE);
            }
        }
    }

    public void setOnSelectedItemChanged(OnSelectedItemChanged onSelectedItemChanged) {
        this.onSelectedItemChanged = onSelectedItemChanged;
    }

    private class OnLoginButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
