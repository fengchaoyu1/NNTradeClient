package zhaohg.testable;

import android.support.v4.app.Fragment;

public class TestableFragment extends Fragment implements Testable {

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
