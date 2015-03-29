package zhaohg.test.helper;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.post.GetPost;
import zhaohg.api.post.GetPostPostEvent;
import zhaohg.api.post.Post;

public class GetPostHelper {

    private Context context;
    private Post post;

    public GetPostHelper(Context context) {
        this.context = context;
    }

    public Post getSellPost(String postId) throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        GetPost newPost = new GetPost(context);
        newPost.setParameter(postId);
        newPost.setEvent(new GetPostPostEvent() {
            @Override
            public void onSuccess(Post post) {
                GetPostHelper.this.post = post;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        newPost.request();
        signal.await();
        return post;
    }

}
