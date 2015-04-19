package zhaohg.test.api.image;

import android.graphics.Bitmap;
import android.test.InstrumentationTestCase;
import android.widget.ImageView;
import zhaohg.api.image.ImageDownloader;


public class TestDownload extends InstrumentationTestCase{
        ImageView myView3 = null;
        Bitmap bmp;
        public void testDownload() throws Exception {
            String imagePath = "http://ts2.mm.bing.net/th?id=HN.608007102913186522&pid=1.7";
            ImageDownloader imageDownloader = new ImageDownloader(imagePath,myView3,bmp);
            imageDownloader.execute();
            assertFalse(imageDownloader.equals(null));
            assertFalse(imagePath.equals(null));
        }
}
