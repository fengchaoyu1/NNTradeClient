package zhaohg.trade;

import android.app.Activity;

public class TestableActivity extends Activity {

    private OnTestFinishedListener testFinishedListener;

    public void finishTest() {
        if (this.testFinishedListener != null) {
            this.testFinishedListener.onTaskFinished();
        }
    }

    public void setOnTestFinishedListener(OnTestFinishedListener listener) {
        this.testFinishedListener = listener;
    }
}
