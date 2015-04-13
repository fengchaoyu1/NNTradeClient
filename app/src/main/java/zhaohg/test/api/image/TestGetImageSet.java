package zhaohg.test.api.image;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.GetImageSet;
import zhaohg.api.image.GetImageSetPostEvent;
import zhaohg.api.image.Image;
import zhaohg.api.post.Post;
import zhaohg.test.helper.GetPostHelper;
import zhaohg.test.helper.NewPostHelper;
import zhaohg.test.helper.RegisterAndLogin;
public class TestGetImageSet extends InstrumentationTestCase {

    private static final int IMAGE_NUM_IN_SET = 10;

    private List<Image> localImages;

    private Context context;
    private int localErrno;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_get_image_set_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    private String newImageSet() throws Exception {
        NewPostHelper newPostHelper = new NewPostHelper(context);
        newPostHelper.setTitle("Title");
        newPostHelper.setDescription("Description");
        newPostHelper.addNewImages(IMAGE_NUM_IN_SET);
        String postId = newPostHelper.newPost();
        GetPostHelper getPostHelper = new GetPostHelper(context);
        Post post = getPostHelper.getSellPost(postId);
        return post.getImageSetId();
    }

    public void testGetImageSetNormal() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final String setId = newImageSet();
        GetImageSet getImageSet = new GetImageSet(context);
        getImageSet.setParameter(setId);
        getImageSet.setEvent(new GetImageSetPostEvent() {
            @Override
            public void onSuccess(List<Image> images) {
                localImages = images;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        getImageSet.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals(IMAGE_NUM_IN_SET, localImages.size());
    }

    public void testGetImageSetNotExist() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final String setId = newImageSet();
        GetImageSet getImageSet = new GetImageSet(context);
        getImageSet.setParameter(setId + "000");
        getImageSet.setEvent(new GetImageSetPostEvent() {
            @Override
            public void onSuccess(List<Image> images) {
                localImages = images;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        getImageSet.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }
}
