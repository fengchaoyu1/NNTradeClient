package zhaohg.test;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.AccountLogin;
import zhaohg.api.AccountLoginPostEvent;
import zhaohg.api.AccountRegister;
import zhaohg.api.AccountRegisterPostEvent;
import zhaohg.api.ApiErrno;
import zhaohg.api.Encryption;

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
            username = this.generateRandomName();
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

    private String generateRandomName() {
        Calendar calendar = Calendar.getInstance();
        String text = "login_" + calendar.getTimeInMillis();
        return "login_" + Encryption.md5(text);
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
    }

    public void testLoginNonExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        AccountLogin login = new AccountLogin(context);
        login.setParameter(this.generateRandomName(), password);
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
    }

}
