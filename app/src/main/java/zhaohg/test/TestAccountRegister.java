package zhaohg.test;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.AccountRegister;
import zhaohg.api.AccountRegisterPostEvent;
import zhaohg.api.ApiErrno;
import zhaohg.api.Encryption;

public class TestAccountRegister extends InstrumentationTestCase {

    private int localErrno;

    @Override
    protected void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateRandomName() {
        Calendar calendar = Calendar.getInstance();
        String text = "test_" + calendar.getTimeInMillis();
        return "test_" + Encryption.md5(text);
    }

    public void testRegisterNormal() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        String username = this.generateRandomName();
        String password = "password";
        AccountRegister register = new AccountRegister(context);
        register.setParameter(username, password);
        register.setEvent(new AccountRegisterPostEvent() {
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
        register.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testRegisterExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        final String username = this.generateRandomName();
        final String password = "password";
        AccountRegister register = new AccountRegister(context);
        register.setParameter(username, password);
        register.setEvent(new AccountRegisterPostEvent() {
            @Override
            public void onSuccess() {
                AccountRegister register = new AccountRegister(context);
                register.setParameter(username, password);
                register.setEvent(new AccountRegisterPostEvent() {
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
                register.request();
            }

            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        register.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_USERNAME_EXIST, localErrno);
    }

}
