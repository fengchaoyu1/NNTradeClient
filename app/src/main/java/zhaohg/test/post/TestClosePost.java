package zhaohg.test.post;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;

import zhaohg.main.R;
import zhaohg.post.PostActivity;
import zhaohg.test.helper.GetPostHelper;
import zhaohg.test.helper.NewPostHelper;
import zhaohg.test.helper.RandomName;
import zhaohg.test.helper.RegisterAndLogin;
import zhaohg.testable.OnTestFinishedListener;

public class TestClosePost extends ActivityInstrumentationTestCase2<PostActivity> {

    private PostActivity activity;
    private Context context;

    private TextView textError;
    private Switch switchOpen;

    public TestClosePost() {
        super(PostActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.context = this.activity.getApplicationContext();

        this.switchOpen = (Switch) this.activity.findViewById(R.id.switch_open);
        this.textError = (TextView) this.activity.findViewById(R.id.text_error);

        RegisterAndLogin registerAndLogin = new RegisterAndLogin(this.context, "test_close_post_");
        registerAndLogin.registerAndLogin();
    }

    public void testClosePostNormal() throws Exception {
        NewPostHelper newPostHelper = new NewPostHelper(context);
        newPostHelper.setTitle(RandomName.generateRandomName("title_"));
        newPostHelper.setDescription(RandomName.generateRandomName("description_"));
        final String postId = newPostHelper.newPost();
        assertFalse(postId.isEmpty());
        final CountDownLatch signal1 = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setPostId(postId);
                activity.loadInfo();
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal1.countDown();
                    }
                });
            }
        });
        signal1.await();
        assertEquals(View.GONE, this.textError.getVisibility());
        assertTrue(switchOpen.isChecked());
        final CountDownLatch signal2 = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchOpen.performClick();
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal2.countDown();
                    }
                });
            }
        });
        signal2.await();
        assertFalse(switchOpen.isChecked());
        GetPostHelper getPostHelper = new GetPostHelper(context);
        assertFalse(getPostHelper.getSellPost(activity.getPostId()).isOpen());
        final CountDownLatch signal3 = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchOpen.performClick();
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal3.countDown();
                    }
                });
            }
        });
        signal3.await();
        assertTrue(switchOpen.isChecked());
        assertTrue(getPostHelper.getSellPost(activity.getPostId()).isOpen());
    }
}
