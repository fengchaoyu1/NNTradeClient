package zhaohg.test.api.sell;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.sell.DeleteSellPost;
import zhaohg.api.sell.DeleteSellPostPostEvent;
import zhaohg.api.sell.GetSellPost;
import zhaohg.api.sell.GetSellPostPostEvent;
import zhaohg.api.sell.NewSellPost;
import zhaohg.api.sell.NewSellPostPostEvent;
import zhaohg.api.sell.SellPost;
import zhaohg.test.helper.RegisterAndLogin;

public class TestDeleteSellPost extends InstrumentationTestCase {

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
        NewSellPost newPost = new NewSellPost(context);
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        newPost.setParameter(title, description, imageIdList);
        newPost.setEvent(new NewSellPostPostEvent() {
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
        DeleteSellPost deletePost = new DeleteSellPost(context);
        deletePost.setParameter(lastPostId);
        deletePost.setEvent(new DeleteSellPostPostEvent() {
            @Override
            public void onSuccess() {
                final GetSellPost getPost = new GetSellPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetSellPostPostEvent() {
                    @Override
                    public void onSuccess(SellPost post) {
                        GetSellPost getPost = new GetSellPost(context);
                        getPost.setParameter(lastPostId);
                        getPost.setEvent(new GetSellPostPostEvent() {
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
        DeleteSellPost deletePost = new DeleteSellPost(context);
        deletePost.setParameter(lastPostId);
        deletePost.setEvent(new DeleteSellPostPostEvent() {
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
        DeleteSellPost deleteSellPost = new DeleteSellPost(context);
        deleteSellPost.setParameter(lastPostId + "123");
        deleteSellPost.setEvent(new DeleteSellPostPostEvent() {
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
        deleteSellPost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_EXIST, localErrno);
    }
}
