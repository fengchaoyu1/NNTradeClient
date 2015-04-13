package zhaohg.test.api.image;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.GetImage;
import zhaohg.api.image.GetImagePostEvent;
import zhaohg.api.image.Image;
import zhaohg.api.image.UploadImage;
import zhaohg.api.image.UploadImagePostEvent;
import zhaohg.test.helper.CreateImage;
import zhaohg.test.helper.RegisterAndLogin;

public class TestGetImage extends InstrumentationTestCase {

    private Image localImage;

    private String localImageId;

    private Context context;
    private int localErrno;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_get_image_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    private void uploadImage() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        CreateImage createImage = new CreateImage();
        UploadImage uploadImage = new UploadImage(context);
        File imageFile = new File(createImage.getImagePath());
        File thumbnailFile = new File(createImage.getThumbnailPath());
        uploadImage.setParameter(imageFile, thumbnailFile);
        uploadImage.setEvent(new UploadImagePostEvent() {
            @Override
            public void onSuccess(String imageId) {
                localImageId = imageId;
                signal.countDown();
            }

            @Override
            public void onFailure(int errno) {
                localErrno = errno;
                signal.countDown();
            }
        });
        uploadImage.request();
        signal.await();
        assertEquals(ApiErrno.ERRNO_NO_ERROR, localErrno);
    }

    public void testGetImageNormal() throws Exception {
        this.uploadImage();
        final CountDownLatch signal = new CountDownLatch(1);
        GetImage getImage = new GetImage(context);
        getImage.setParameter(localImageId);
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
        assertEquals(localImage.getImageId(), localImageId);
        assertTrue(localImage.getImagePath().length() > 0);
        assertTrue(localImage.getThumbnailPath().length() > 0);
    }

    public void testGetImageNotExist() throws Exception {
        this.uploadImage();
        final CountDownLatch signal = new CountDownLatch(1);
        GetImage getImage = new GetImage(context);
        getImage.setParameter(localImageId + "000");
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
