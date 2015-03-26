package zhaohg.test.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.sell.NewSellPost;
import zhaohg.api.sell.NewSellPostPostEvent;

public class AddNewSellPost {

    private Context context;

    private String title;
    private String description;
    private List<String> imageIdList;

    private String postId;

    public AddNewSellPost(Context context) {
        this.context = context;
        this.title = "";
        this.description = "";
        this.imageIdList = new ArrayList<>();
        this.postId = "";
    }

    public String newPost() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        NewSellPost newPost = new NewSellPost(context);
        newPost.setParameter(title, description, imageIdList);
        newPost.setEvent(new NewSellPostPostEvent() {
            @Override
            public void onSuccess(final String id) {
                postId = id;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                postId = "";
                signal.countDown();
            }
        });
        newPost.request();
        signal.await();
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageIdList() {
        return imageIdList;
    }

    public void setImageIdList(List<String> imageIdList) {
        this.imageIdList = imageIdList;
    }

}