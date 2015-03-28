package zhaohg.test.api.post;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.post.GetPost;
import zhaohg.api.post.GetPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.test.helper.RegisterAndLogin;

public class TestGetPost extends InstrumentationTestCase {

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
        GetPost newPost = new GetPost(context);
        newPost.setParameter("1234567890");
        newPost.setEvent(new GetPostPostEvent() {
            @Override
            public void onSuccess(Post post) {
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
