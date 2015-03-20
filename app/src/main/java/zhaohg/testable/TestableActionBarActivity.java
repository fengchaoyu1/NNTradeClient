package zhaohg.testable;

import android.support.v7.app.ActionBarActivity;

public class TestableActionBarActivity extends ActionBarActivity implements Testable {

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