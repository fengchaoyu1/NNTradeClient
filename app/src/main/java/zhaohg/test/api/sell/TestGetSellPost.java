package zhaohg.test.api.sell;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.sell.GetSellPost;
import zhaohg.api.sell.GetSellPostPostEvent;
import zhaohg.api.sell.SellPost;
import zhaohg.test.helper.RegisterAndLogin;

public class TestGetSellPost extends InstrumentationTestCase {

    private int localErrno;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Context context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_get_sell_post_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    public void testGetPostNotExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        Context context = this.getInstrumentation().getContext();
        GetSellPost newPost = new GetSellPost(context);
        newPost.setParameter("1234567890");
        newPost.setEvent(new GetSellPostPostEvent() {
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
