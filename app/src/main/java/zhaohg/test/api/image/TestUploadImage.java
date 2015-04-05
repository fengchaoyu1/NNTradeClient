package zhaohg.test.api.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.UploadImage;
import zhaohg.api.image.UploadImagePostEvent;
import zhaohg.test.helper.RegisterAndLogin;

public class TestUploadImage extends InstrumentationTestCase {

    private String imagePath = "";
    private String thumbnailPath = "";

    private Context context;
    private int localErrno;

    private String localImageId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = this.getInstrumentation().getContext();
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(context, "test_upload_image_");
        assertTrue(registerAndLogin.registerAndLogin());
    }

    private void createImage() throws Exception {
        imagePath = Environment.getExternalStorageDirectory() + File.separator + "test_image.jpg";
        Bitmap bitmap = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap.setPixel(i, j, Color.argb(255,
                        255 * i / bitmap.getWidth(),
                        255 - 255 * i / bitmap.getWidth(),
                        255 * j / bitmap.getHeight()));
            }
        }
        FileOutputStream outputStream = new FileOutputStream(imagePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
        thumbnailPath = Environment.getExternalStorageDirectory() + File.separator + "test_thumbnail.jpg";
        bitmap = Bitmap.createBitmap(200, 150, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap.setPixel(i, j, Color.argb(255,
                        255 * i / bitmap.getWidth(),
                        255 - 255 * i / bitmap.getWidth(),
                        255 * j / bitmap.getHeight()));
            }
        }
        outputStream = new FileOutputStream(thumbnailPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
    }

    public void testNewPostNormal() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        this.createImage();
        UploadImage uploadImage = new UploadImage(context);
        File imageFile = new File(imagePath);
        File thumbnailFile = new File(thumbnailPath);
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
}
