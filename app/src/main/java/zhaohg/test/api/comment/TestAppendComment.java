package zhaohg.test.api.comment;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.comment.AppendComment;
import zhaohg.api.comment.AppendCommentPostEvent;
import zhaohg.api.comment.Comment;
import zhaohg.api.comment.GetComments;
import zhaohg.api.comment.GetCommentsPostEvent;
import zhaohg.api.post.Post;
import zhaohg.test.helper.GetPostHelper;
import zhaohg.test.helper.NewPostHelper;
import zhaohg.test.helper.RegisterAndLogin;

public class TestAppendComment extends InstrumentationTestCase {

    private Context context;
    private int localErrno;

    private String localCommentId;
    private List<Comment> localComments;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_append_comment");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    private String createComments() throws Exception {
        NewPostHelper newPostHelper = new NewPostHelper(context);
        newPostHelper.setTitle("加入");
        newPostHelper.setDescription("评论");
        String postId = newPostHelper.newPost();
        GetPostHelper getPostHelper = new GetPostHelper(context);
        Post post = getPostHelper.getSellPost(postId);
        return post.getCommentsId();
    }

    public void testNewPostNormal() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final String commentsId = this.createComments();
        AppendComment appendComment = new AppendComment(context);
        appendComment.setParameter(commentsId, "沙发");
        appendComment.setEvent(new AppendCommentPostEvent() {
            @Override
            public void onSuccess() {
                GetComments getComments = new GetComments(context);
                getComments.setParameter(commentsId, 1);
                getComments.setEvent(new GetCommentsPostEvent() {
                    @Override
                    public void onSuccess(List<Comment> comments, boolean isEnd) {
                        localComments = comments;
                        signal.countDown();
                    }
                    @Override
                    public void onFailure(int errno) {
                        localErrno = errno;
                        signal.countDown();
                    }
                });
                getComments.request();
            }
            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        appendComment.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals(1, this.localComments.size());
        Comment comment = this.localComments.get(0);
        assertEquals(appendComment.loadUserId(), comment.getUserId());
        assertEquals(appendComment.loadUsername(), comment.getUserName());
        assertEquals("沙发", comment.getMessage());
        assertFalse(comment.isReply());
    }

    public void testNewPostNormalTwice() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final String commentsId = this.createComments();
        AppendComment appendComment = new AppendComment(context);
        appendComment.setParameter(commentsId, "沙发");
        appendComment.setEvent(new AppendCommentPostEvent() {
            @Override
            public void onSuccess() {
                AppendComment appendComment = new AppendComment(context);
                appendComment.setParameter(commentsId, "板凳");
                appendComment.setEvent(new AppendCommentPostEvent() {
                    @Override
                    public void onSuccess() {
                        GetComments getComments = new GetComments(context);
                        getComments.setParameter(commentsId, 1);
                        getComments.setEvent(new GetCommentsPostEvent() {
                            @Override
                            public void onSuccess(List<Comment> comments, boolean isEnd) {
                                localComments = comments;
                                signal.countDown();
                            }

                            @Override
                            public void onFailure(int errno) {
                                localErrno = errno;
                                signal.countDown();
                            }
                        });
                        getComments.request();
                    }

                    @Override
                    public void onFailure(int errno) {
                        localErrno = errno;
                        signal.countDown();
                    }
                });
                appendComment.request();
            }
            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        appendComment.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals(2, this.localComments.size());
        Comment comment = this.localComments.get(0);
        assertEquals(appendComment.loadUserId(), comment.getUserId());
        assertEquals(appendComment.loadUsername(), comment.getUserName());
        assertEquals("沙发", comment.getMessage());
        assertFalse(comment.isReply());
        comment = this.localComments.get(1);
        assertEquals(appendComment.loadUserId(), comment.getUserId());
        assertEquals(appendComment.loadUsername(), comment.getUserName());
        assertEquals("板凳", comment.getMessage());
        assertFalse(comment.isReply());
    }

    public void testNewPostNormalReply() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        final String commentsId = this.createComments();
        AppendComment appendComment = new AppendComment(context);
        appendComment.setParameter(commentsId, "沙发");
        appendComment.setEvent(new AppendCommentPostEvent() {
            @Override
            public void onSuccess() {
                GetComments getComments = new GetComments(context);
                getComments.setParameter(commentsId, 1);
                getComments.setEvent(new GetCommentsPostEvent() {
                    @Override
                    public void onSuccess(List<Comment> comments, boolean isEnd) {
                        localCommentId = comments.get(0).getCommentId();
                        AppendComment appendComment = new AppendComment(context);
                        appendComment.setParameter(commentsId, "板凳", localCommentId);
                        appendComment.setEvent(new AppendCommentPostEvent() {
                            @Override
                            public void onSuccess() {
                                GetComments getComments = new GetComments(context);
                                getComments.setParameter(commentsId, 1);
                                getComments.setEvent(new GetCommentsPostEvent() {
                                    @Override
                                    public void onSuccess(List<Comment> comments, boolean isEnd) {
                                        localComments = comments;
                                        signal.countDown();
                                    }

                                    @Override
                                    public void onFailure(int errno) {
                                        localErrno = errno;
                                        signal.countDown();
                                    }
                                });
                                getComments.request();
                            }

                            @Override
                            public void onFailure(int errno) {
                                localErrno = errno;
                                signal.countDown();
                            }
                        });
                        appendComment.request();
                    }
                    @Override
                    public void onFailure(int errno) {
                        localErrno = errno;
                        signal.countDown();
                    }
                });
                getComments.request();
            }
            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        appendComment.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals(2, this.localComments.size());
        Comment comment = this.localComments.get(0);
        assertEquals(appendComment.loadUserId(), comment.getUserId());
        assertEquals(appendComment.loadUsername(), comment.getUserName());
        assertEquals("沙发", comment.getMessage());
        assertFalse(comment.isReply());
        comment = this.localComments.get(1);
        assertEquals(appendComment.loadUserId(), comment.getUserId());
        assertEquals(appendComment.loadUsername(), comment.getUserName());
        assertEquals("板凳", comment.getMessage());
        assertTrue(comment.isReply());
        assertEquals(localCommentId, comment.getReplyCommentId());
        assertEquals(appendComment.loadUserId(), comment.getReplyUserId());
        assertEquals(appendComment.loadUsername(), comment.getReplyUserName());
    }

}