package zhaohg.test.helper;

import android.content.Context;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.UploadImagePostEvent;

public class UploadImageHelper {

    private Context context;

    private int localErrno;
    private String localImageId;

    public UploadImageHelper(Context context) {
        this.context = context;
    }

    public String uploadImage() throws Exception {
        this.localErrno = ApiErrno.ERRNO_NO_ERROR;
        final CountDownLatch signal = new CountDownLatch(1);
        CreateImage createImage = new CreateImage();
        zhaohg.api.image.UploadImage uploadImage = new zhaohg.api.image.UploadImage(context);
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
        return localImageId;
    }

}
