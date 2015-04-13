package zhaohg.test.api.image;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.UploadImage;
import zhaohg.api.image.UploadImagePostEvent;
import zhaohg.test.helper.CreateImage;
import zhaohg.test.helper.RegisterAndLogin;

public class TestUploadImage extends InstrumentationTestCase {

    private Context context;
    private int localErrno;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_upload_image_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    public void testUploadImageNormal() throws Exception {
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
}
