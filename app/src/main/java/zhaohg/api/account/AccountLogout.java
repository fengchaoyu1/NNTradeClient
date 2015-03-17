package zhaohg.api.account;

import android.content.Context;

import zhaohg.api.ApiBase;

public class AccountLogout extends ApiBase {

    private AccountLogoutPostEvent event;

    public AccountLogout(Context context) {
        super(context);
    }

    public void setEvent(AccountLogoutPostEvent event) {
        this.event = event;
    }

    @Override
    public void request() {
        this.saveToken("");
        if (event != null) {
            event.onSuccess();
        }
    }
}
