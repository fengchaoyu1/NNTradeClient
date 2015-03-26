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
import zhaohg.api.sell.UpdateSellPost;
import zhaohg.api.sell.UpdateSellPostPostEvent;
import zhaohg.test.helper.RegisterAndLogin;

public class TestUpdateSellPost extends InstrumentationTestCase {

    private int localErrno;

    private String username;
    private String password;

    private String lastPostId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Context context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_update_sell_post_");
        assertTrue(registerAndLogin.registerAndLogin());
        username = registerAndLogin.getUsername();
        password = registerAndLogin.getPassword();
    }

    private String generateRandomName() {
        Calendar calendar = Calendar.getInstance();
        String text = "update_post_" + calendar.getTimeInMillis();
        return "update_post_" + Encryption.md5(text);
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

    public void testUpdateTitle() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        this.newSellPost(title, description, imageIdList);
        UpdateSellPost updatePost = new UpdateSellPost(context);
        updatePost.setParameter(lastPostId);
        final String newTitle = "Update New Title";
        updatePost.setUpdateTitle(newTitle);
        updatePost.setEvent(new UpdateSellPostPostEvent() {
            @Override
            public void onSuccess() {
                final GetSellPost getPost = new GetSellPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetSellPostPostEvent() {
                    @Override
                    public void onSuccess(SellPost post) {
                        assertEquals(newTitle, post.getTitle());
                        assertEquals(description, post.getDescription());
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
        updatePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testUpdateDescription() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        this.newSellPost(title, description, imageIdList);
        UpdateSellPost updatePost = new UpdateSellPost(context);
        updatePost.setParameter(lastPostId);
        final String newDescription = "Update New Description";
        updatePost.setUpdateDescription(newDescription);
        updatePost.setEvent(new UpdateSellPostPostEvent() {
            @Override
            public void onSuccess() {
                final GetSellPost getPost = new GetSellPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetSellPostPostEvent() {
                    @Override
                    public void onSuccess(SellPost post) {
                        assertEquals(title, post.getTitle());
                        assertEquals(newDescription, post.getDescription());
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
        updatePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testUpdateClose() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        this.newSellPost(title, description, imageIdList);
        UpdateSellPost updatePost = new UpdateSellPost(context);
        updatePost.setParameter(lastPostId);
        updatePost.setUpdateOpen(false);
        updatePost.setEvent(new UpdateSellPostPostEvent() {
            @Override
            public void onSuccess() {
                final GetSellPost getPost = new GetSellPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetSellPostPostEvent() {
                    @Override
                    public void onSuccess(SellPost post) {
                        assertEquals(title, post.getTitle());
                        assertEquals(description, post.getDescription());
                        assertFalse(post.isOpen());
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
        updatePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testUpdateNotOwner() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        this.newSellPost(title, description, imageIdList);
        username = generateRandomName();
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
                        UpdateSellPost updatePost = new UpdateSellPost(context);
                        updatePost.setParameter(lastPostId);
                        updatePost.setUpdateOpen(false);
                        updatePost.setEvent(new UpdateSellPostPostEvent() {
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
                        updatePost.request();
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
        assertEquals(ApiErrno.ERRNO_NOT_OWNER, localErrno);
    }

    public void testUpdateNotExist() throws InterruptedException {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final Context context = this.getInstrumentation().getContext();
        List<String> imageIdList = new ArrayList<>();
        imageIdList.add("1");
        imageIdList.add("2");
        imageIdList.add("3");
        final String title = "New Post+";
        final String description = "New Post Description";
        this.newSellPost(title, description, imageIdList);
        UpdateSellPost updatePost = new UpdateSellPost(context);
        updatePost.setParameter(lastPostId + "123");
        updatePost.setUpdateOpen(false);
        updatePost.setEvent(new UpdateSellPostPostEvent() {
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
        updatePost.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_EXIST, localErrno);
    }
}
