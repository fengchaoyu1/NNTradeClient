package zhaohg.test.api.sell;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.Encryption;
import zhaohg.api.account.AccountLogin;
import zhaohg.api.account.AccountLoginPostEvent;
import zhaohg.api.account.AccountRegister;
import zhaohg.api.account.AccountRegisterPostEvent;
import zhaohg.api.sell.GetSellPost;
import zhaohg.api.sell.GetSellPostPostEvent;
import zhaohg.api.sell.NewSellPost;
import zhaohg.api.sell.NewSellPostPostEvent;
import zhaohg.api.sell.SellPost;

public class TestNewSellPost extends InstrumentationTestCase {

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
        String text = "new_post_" + calendar.getTimeInMillis();
        return "new_post_" + Encryption.md5(text);
    }

    public void testNewPostNormal() throws InterruptedException {
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
            public void onSuccess(final String post_id) {
                assertFalse(post_id.equals(""));
                final GetSellPost getPost = new GetSellPost(context);
                getPost.setParameter(post_id);
                getPost.setEvent(new GetSellPostPostEvent() {
                    @Override
                    public void onSuccess(SellPost post) {
                        assertEquals(getPost.loadUserId(), post.getUserId());
                        assertEquals(post_id, post.getPostId());
                        assertEquals(title, post.getTitle());
                        assertEquals(description, post.getDescription());
                        assertFalse(post.getPostId().equals(""));
                        assertFalse(post.getImageSetId().equals(""));
                        assertTrue(post.isOpen());
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
        newPost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testNewPostUtf8() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        NewSellPost newPost = new NewSellPost(context);
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "出售信息";
        final String description = "出售内容描述";
        newPost.setParameter(title, description, imageIdList);
        newPost.setEvent(new NewSellPostPostEvent() {
            @Override
            public void onSuccess(final String post_id) {
                assertFalse(post_id.equals(""));
                final GetSellPost getPost = new GetSellPost(context);
                getPost.setParameter(post_id);
                getPost.setEvent(new GetSellPostPostEvent() {
                    @Override
                    public void onSuccess(SellPost post) {
                        assertEquals(getPost.loadUserId(), post.getUserId());
                        assertEquals(post_id, post.getPostId());
                        assertEquals(title, post.getTitle());
                        assertEquals(description, post.getDescription());
                        assertFalse(post.getPostId().equals(""));
                        assertFalse(post.getImageSetId().equals(""));
                        assertTrue(post.isOpen());
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
        newPost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

}
