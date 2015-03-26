package zhaohg.test.api.account;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.api.ApiErrno;
import zhaohg.test.helper.RandomName;

public class TestAccountLogin extends InstrumentationTestCase {

    private int localErrno;

    private String username;
    private String password;

    @Override
    protected void setUp() {
        try {
            super.setUp();
            final CountDownLatch signal = new CountDownLatch(1);
            Context context = this.getInstrumentation().getContext();
            username = RandomName.generateRandomName("test_account_login_");
            password = "password";
            AccountRegister register = new AccountRegister(context);
            register.setParameter(username, password);
            register.setEvent(new AccountRegisterPostEvent() {
                @Override
                public void onSuccess() {
                    signal.countDown();
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

    public void testLoginNormal() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        AccountLogin login = new AccountLogin(context);
        login.setParameter(username, password);
        login.setEvent(new AccountLoginPostEvent() {
            @Override
            public void onSuccess() {
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        login.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertTrue(login.hasLogin());
        assertFalse(login.loadToken().equals(""));
        assertEquals(username, login.loadUsername());
    }

    public void testLoginNonExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        AccountLogin login = new AccountLogin(context);
        login.setParameter(RandomName.generateRandomName("test_account_login_"), password);
        login.setEvent(new AccountLoginPostEvent() {
            @Override
            public void onSuccess() {
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        login.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_USERNAME_NON_EXIST, localErrno);
        assertFalse(login.hasLogin());
    }

    public void testLoginWrongPassword() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        AccountLogin login = new AccountLogin(context);
        login.setParameter(username, "error_" + password);
        login.setEvent(new AccountLoginPostEvent() {
            @Override
            public void onSuccess() {
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        login.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_MISMATCH_USERNAME_PASSWORD, localErrno);
        assertFalse(login.hasLogin());
    }

}
