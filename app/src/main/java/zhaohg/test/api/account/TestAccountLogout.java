package zhaohg.test.api.account;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountLogout;
import zhaohg.api.account.AccountLogoutPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.test.helper.RandomName;

public class TestAccountLogout extends InstrumentationTestCase {

    @Override
    protected void setUp() {
        try {
            super.setUp();
            final CountDownLatch signal = new CountDownLatch(1);
            final Context context = this.getInstrumentation().getContext();
            final String username = RandomName.generateRandomName("test_account_logout_");
            final String password = "password";
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
            register.request();
            signal.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testLogoutNormal() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        AccountLogout logout = new AccountLogout(context);
        logout.setEvent(new AccountLogoutPostEvent() {
            @Override
            public void onSuccess() {
                signal.countDown();
            }
        });
        logout.request();
        signal.await();
        assertEquals("", logout.loadToken());
    }

}
