package zhaohg.test.api.post;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.post.DeletePost;
import zhaohg.api.post.DeletePostPostEvent;
import zhaohg.api.post.GetPost;
import zhaohg.api.post.GetPostPostEvent;
import zhaohg.api.post.NewPost;
import zhaohg.api.post.NewPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.test.helper.RegisterAndLogin;

public class TestDeletePost extends InstrumentationTestCase {

    private int localErrno;

    private String lastPostId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Context context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_delete_post_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    private void newSellPost() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        NewPost newPost = new NewPost(context);
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        newPost.setParameter(Post.POST_TYPE_SELL, title, description, imageIdList);
        newPost.setEvent(new NewPostPostEvent() {
            @Override
            public void onSuccess(final String postId) {
                lastPostId = postId;
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

    public void testDeleteNormal() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        this.newSellPost();
        DeletePost deletePost = new DeletePost(context);
        deletePost.setParameter(lastPostId);
        deletePost.setEvent(new DeletePostPostEvent() {
            @Override
            public void onSuccess() {
                final GetPost getPost = new GetPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetPostPostEvent() {
                    @Override
                    public void onSuccess(Post post) {
                        GetPost getPost = new GetPost(context);
                        getPost.setParameter(lastPostId);
                        getPost.setEvent(new GetPostPostEvent() {
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
                        getPost.request();
                    }

                    @Override
                    public void onFailure(int errno) {
                        localErrno = errno;
                        signal.countDown();
                    }
                });
                getPost.request();
            }
            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        deletePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_EXIST, localErrno);
    }

    public void testDeleteNotOwner() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        this.newSellPost();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_delete_post_");
        assertTrue(registerAndLogin.registerAndLogin());
        DeletePost deletePost = new DeletePost(context);
        deletePost.setParameter(lastPostId);
        deletePost.setEvent(new DeletePostPostEvent() {
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
        deletePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_OWNER, localErrno);
    }

    public void testDeleteNotExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        this.newSellPost();
        DeletePost deletePost = new DeletePost(context);
        deletePost.setParameter(lastPostId + "123");
        deletePost.setEvent(new DeletePostPostEvent() {
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
        deletePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_EXIST, localErrno);
    }
}
