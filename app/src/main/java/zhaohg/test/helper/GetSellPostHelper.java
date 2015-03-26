package zhaohg.test.helper;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.sell.GetSellPostPostEvent;
import zhaohg.api.sell.SellPost;

public class GetSellPostHelper {

    private Context context;
    private SellPost sellPost;

    public GetSellPostHelper(Context context) {
        this.context = context;
    }

    public SellPost getSellPost(String postId) throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        zhaohg.api.sell.GetSellPost newPost = new zhaohg.api.sell.GetSellPost(context);
        newPost.setParameter(postId);
        newPost.setEvent(new GetSellPostPostEvent() {
            @Override
            public void onSuccess(SellPost post) {
                sellPost = post;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                signal.countDown();
            }
        });
        newPost.request();
        signal.await();
        return sellPost;
    }

}
