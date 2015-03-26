package zhaohg.test.sell;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;

import zhaohg.main.R;
import zhaohg.sell.SellPostActivity;
import zhaohg.test.helper.AddNewSellPost;
import zhaohg.test.helper.RandomName;
import zhaohg.test.helper.RegisterAndLogin;
import zhaohg.testable.OnTestFinishedListener;

public class TestGetPost extends ActivityInstrumentationTestCase2<SellPostActivity> {

    private SellPostActivity activity;
    private Context context;

    private TextView textTitle;
    private TextView textDescription;
    private TextView textError;

    public TestGetPost() {
        super(SellPostActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.context = this.activity.getApplicationContext();

        this.textTitle = (TextView) this.activity.findViewById(R.id.text_title);
        this.textDescription = (TextView) this.activity.findViewById(R.id.text_description);
        this.textError = (TextView) this.activity.findViewById(R.id.text_error);

        RegisterAndLogin registerAndLogin = new RegisterAndLogin(this.context, "test_get_post_");
        registerAndLogin.registerAndLogin();
    }

    public void testGetPostNormal() throws Exception {
        AddNewSellPost addNewSellPost = new AddNewSellPost(context);
        final String title = RandomName.generateRandomName("title_");
        final String description = RandomName.generateRandomName("description_");
        addNewSellPost.setTitle(title);
        addNewSellPost.setDescription(description);
        final String postId = addNewSellPost.newPost();
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
        assertEquals(View.VISIBLE, this.textTitle.getVisibility());
        assertEquals(View.VISIBLE, this.textDescription.getVisibility());
        assertEquals(View.GONE, this.textError.getVisibility());
        assertEquals(title, this.textTitle.getText());
        assertEquals(description, this.textDescription.getText());
    }

    public void testGetPostNotExist() throws Exception {
        AddNewSellPost addNewSellPost = new AddNewSellPost(context);
        final String title = RandomName.generateRandomName("title_");
        final String description = RandomName.generateRandomName("description_");
        addNewSellPost.setTitle(title);
        addNewSellPost.setDescription(description);
        final String postId = addNewSellPost.newPost();
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
        assertEquals(View.GONE, this.textTitle.getVisibility());
        assertEquals(View.GONE, this.textDescription.getVisibility());
        assertEquals(View.VISIBLE, this.textError.getVisibility());
        assertEquals(context.getString(R.string.error_post_not_exist), this.textError.getText());
    }
}
