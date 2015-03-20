package zhaohg.testable;

import android.app.Activity;

public class TestableActivity extends Activity implements Testable {

    private OnTestFinishedListener testFinishedListener;

    @Override
    public void finishTest() {
        if (this.testFinishedListener != null) {
            this.testFinishedListener.onTaskFinished();
        }
    }

    @Override
    public void setOnTestFinishedListener(OnTestFinishedListener listener) {
        this.testFinishedListener = listener;
    }
}