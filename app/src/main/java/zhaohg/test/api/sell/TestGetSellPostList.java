package zhaohg.test.api.sell;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.sell.GetSellPostList;
import zhaohg.api.sell.GetSellPostListPostEvent;
import zhaohg.api.sell.NewSellPost;
import zhaohg.api.sell.NewSellPostPostEvent;
import zhaohg.api.sell.SellPost;
import zhaohg.test.helper.RegisterAndLogin;

public class TestGetSellPostList extends InstrumentationTestCase {

    private int localErrno;

    List<SellPost> localPosts;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Context context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_get_sell_post_list_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    private void newSellPost(final String title, final String description, List<String> imageIdList) throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        NewSellPost newPost = new NewSellPost(context);
        newPost.setParameter(title, description, imageIdList);
        newPost.setEvent(new NewSellPostPostEvent() {
            @Override
            public void onSuccess(final String postId) {
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
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testGetSellListNormal() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        for (int i = 0; i < 20; ++i) {
            newSellPost("title_" + i, "", new ArrayList<String>());
        }
        GetSellPostList getSellPostList = new GetSellPostList(context);
        getSellPostList.setParameter(1);
        getSellPostList.setEvent(new GetSellPostListPostEvent() {
            @Override
            public void onSuccess(List<SellPost> posts) {
                localPosts = posts;
                signal.countDown();
            }
            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        getSellPostList.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals("title_19", localPosts.get(0).getTitle());
        assertEquals("title_18", localPosts.get(1).getTitle());
    }
}
