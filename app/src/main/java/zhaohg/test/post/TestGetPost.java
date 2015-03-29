package zhaohg.test.post;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;

import zhaohg.main.R;
import zhaohg.post.PostActivity;
import zhaohg.test.helper.NewPostHelper;
import zhaohg.test.helper.RandomName;
import zhaohg.test.helper.RegisterAndLogin;
import zhaohg.testable.OnTestFinishedListener;

public class TestGetPost extends ActivityInstrumentationTestCase2<PostActivity> {

    private PostActivity activity;
    private Context context;

    private TextView textTitle;
    private TextView textDescription;
    private TextView textError;
    private Switch switchOpen;

    public TestGetPost() {
        super(PostActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.context = this.activity.getApplicationContext();

        this.textTitle = (TextView) this.activity.findViewById(R.id.text_title);
        this.textDescription = (TextView) this.activity.findViewById(R.id.text_description);
        this.switchOpen = (Switch) this.activity.findViewById(R.id.switch_open);
        this.textError = (TextView) this.activity.findViewById(R.id.text_error);

        RegisterAndLogin registerAndLogin = new RegisterAndLogin(this.context, "test_get_post_");
        registerAndLogin.registerAndLogin();
    }

    public void testGetPostNormal() throws Exception {
        NewPostHelper newPostHelper = new NewPostHelper(context);
        final String title = RandomName.generateRandomName("title_");
        final String description = RandomName.generateRandomName("description_");
        newPostHelper.setTitle(title);
        newPostHelper.setDescription(description);
        final String postId = newPostHelper.newPost();
        assertFalse(postId.isEmpty());
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setPostId(postId);
                activity.loadInfo();
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal.countDown();
                    }
                });
            }
        });
        signal.await();
        assertEquals(View.GONE, this.textError.getVisibility());
        assertEquals(title, this.textTitle.getText());
        assertEquals(description, this.textDescription.getText());
        assertTrue(description, switchOpen.isChecked());
    }

    public void testGetPostNotExist() throws Exception {
        NewPostHelper newPostHelper = new NewPostHelper(context);
        final String title = RandomName.generateRandomName("title_");
        final String description = RandomName.generateRandomName("description_");
        newPostHelper.setTitle(title);
        newPostHelper.setDescription(description);
        final String postId = newPostHelper.newPost();
        assertFalse(postId.isEmpty());
        final CountDownLatch signal = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setPostId(postId + "0");
                activity.loadInfo();
                activity.setOnTestFinishedListener(new OnTestFinishedListener() {
                    @Override
                    public void onTaskFinished() {
                        signal.countDown();
                    }
                });
            }
        });
        signal.await();
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(context.getString(R.string.error_post_not_exist), this.textError.getText());
    }
}
