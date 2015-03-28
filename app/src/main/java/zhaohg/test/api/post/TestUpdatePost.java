package zhaohg.test.api.post;

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
import zhaohg.api.post.GetPost;
import zhaohg.api.post.GetPostPostEvent;
import zhaohg.api.post.NewPost;
import zhaohg.api.post.NewPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.api.post.UpdatePost;
import zhaohg.api.post.UpdatePostPostEvent;
import zhaohg.test.helper.RegisterAndLogin;

public class TestUpdatePost extends InstrumentationTestCase {

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
        NewPost newPost = new NewPost(context);
        newPost.setParameter(title, description, imageIdList);
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
        UpdatePost updatePost = new UpdatePost(context);
        updatePost.setParameter(lastPostId);
        final String newTitle = "Update New Title";
        updatePost.setUpdateTitle(newTitle);
        updatePost.setEvent(new UpdatePostPostEvent() {
            @Override
            public void onSuccess() {
                final GetPost getPost = new GetPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetPostPostEvent() {
                    @Override
                    public void onSuccess(Post post) {
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
        UpdatePost updatePost = new UpdatePost(context);
        updatePost.setParameter(lastPostId);
        final String newDescription = "Update New Description";
        updatePost.setUpdateDescription(newDescription);
        updatePost.setEvent(new UpdatePostPostEvent() {
            @Override
            public void onSuccess() {
                final GetPost getPost = new GetPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetPostPostEvent() {
                    @Override
                    public void onSuccess(Post post) {
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
        UpdatePost updatePost = new UpdatePost(context);
        updatePost.setParameter(lastPostId);
        updatePost.setUpdateOpen(false);
        updatePost.setEvent(new UpdatePostPostEvent() {
            @Override
            public void onSuccess() {
                final GetPost getPost = new GetPost(context);
                getPost.setParameter(lastPostId);
                getPost.setEvent(new GetPostPostEvent() {
                    @Override
                    public void onSuccess(Post post) {
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
                        UpdatePost updatePost = new UpdatePost(context);
                        updatePost.setParameter(lastPostId);
                        updatePost.setUpdateOpen(false);
                        updatePost.setEvent(new UpdatePostPostEvent() {
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
        UpdatePost updatePost = new UpdatePost(context);
        updatePost.setParameter(lastPostId + "123");
        updatePost.setUpdateOpen(false);
        updatePost.setEvent(new UpdatePostPostEvent() {
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
