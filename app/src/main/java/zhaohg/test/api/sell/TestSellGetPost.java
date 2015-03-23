package zhaohg.test.api.sell;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.Encryption;
import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.api.sell.SellGetPost;
import zhaohg.api.sell.SellGetPostPostEvent;
import zhaohg.api.sell.SellPost;

public class TestSellGetPost extends InstrumentationTestCase {

    private int localErrno;

    private String username;
    private String password;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        username = this.generateRandomName();
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
                        localErrno = errno;
                        signal.countDown();
                    }
                });
                login.request();
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

    private String generateRandomName() {
        Calendar calendar = Calendar.getInstance();
        String text = "get_post_" + calendar.getTimeInMillis();
        return "get_post_" + Encryption.md5(text);
    }

    public void testGetPostNotExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        SellGetPost newPost = new SellGetPost(context);
        newPost.setParameter("1234567890");
        newPost.setEvent(new SellGetPostPostEvent() {
            @Override
            public void onSuccess(SellPost post) {
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        newPost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_EXIST, localErrno);
    }

}
