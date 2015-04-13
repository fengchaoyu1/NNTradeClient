package zhaohg.test.api.image;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.GetImage;
import zhaohg.api.image.GetImagePostEvent;
import zhaohg.api.image.Image;
import zhaohg.test.helper.RegisterAndLogin;
import zhaohg.test.helper.UploadImageHelper;

public class TestGetImage extends InstrumentationTestCase {

    private Image localImage;

    private Context context;
    private int localErrno;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_get_image_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    public void testGetImageNormal() throws Exception {
        UploadImageHelper uploadImageHelper = new UploadImageHelper(context);
        final String imageId = uploadImageHelper.uploadImage();
        final CountDownLatch signal = new CountDownLatch(1);
        GetImage getImage = new GetImage(context);
        getImage.setParameter(imageId);
        getImage.setEvent(new GetImagePostEvent() {
            @Override
            public void onSuccess(Image image) {
                localImage = image;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        getImage.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
        assertEquals(localImage.getImageId(), imageId);
        assertTrue(localImage.getImagePath().length() > 0);
        assertTrue(localImage.getThumbnailPath().length() > 0);
    }

    public void testGetImageNotExist() throws Exception {
        UploadImageHelper uploadImageHelper = new UploadImageHelper(context);
        final String imageId = uploadImageHelper.uploadImage();
        final CountDownLatch signal = new CountDownLatch(1);
        GetImage getImage = new GetImage(context);
        getImage.setParameter(imageId + "000");
        getImage.setEvent(new GetImagePostEvent() {
            @Override
            public void onSuccess(Image image) {
                localImage = image;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        getImage.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NOT_EXIST, localErrno);
    }

}
