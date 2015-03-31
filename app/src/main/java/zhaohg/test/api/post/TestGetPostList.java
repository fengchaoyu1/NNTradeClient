package zhaohg.test.api.post;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.post.GetPostList;
import zhaohg.api.post.GetPostListPostEvent;
import zhaohg.api.post.NewPost;
import zhaohg.api.post.NewPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.test.helper.RegisterAndLogin;

public class TestGetPostList extends InstrumentationTestCase {

    private int localErrno;

    List<Post> localPosts;

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
        NewPost newPost = new NewPost(context);
        newPost.setParameter(title, description, imageIdList);
        newPost.setEvent(new NewPostPostEvent() {
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
        GetPostList getPostList = new GetPostList(context);
        getPostList.setParameter(1);
        getPostList.setEvent(new GetPostListPostEvent() {
            @Override
            public void onSuccess(List<Post> posts, boolean isEnd) {
                localPosts = posts;
                signal.countDown();
            }
            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        getPostList.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals("title_19", localPosts.get(0).getTitle());
        assertEquals("title_18", localPosts.get(1).getTitle());
    }
}
