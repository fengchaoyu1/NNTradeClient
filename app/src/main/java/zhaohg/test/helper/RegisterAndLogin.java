package zhaohg.test.helper;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;

public class RegisterAndLogin {

    private Context context;
    private String prefix;

    private String username;
    private String password;
    private boolean success;

    public RegisterAndLogin(Context context, String prefix) {
        this.context = context;
        this.prefix = prefix;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean registerAndLogin() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        username = RandomName.generateRandomName(prefix);
        password = "password";
        AccountRegister register = new AccountRegister(context);
        register.setParameter(username, password);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                AccountLogin login = new AccountLogin(context);
                login.setParameter(username, password);
                login.setEvent(new AccountLoginPostEvent() {
                    @Override
                    public void onSuccess() {
                        signal.countDown();
                    }

                    @Override
                    public void onFailure(int errno) {
                        success = false;
                        signal.countDown();
                    }
                });
                login.request();
            }

            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        success = true;
        register.request();
        signal.await();
        return success;
    }
}
