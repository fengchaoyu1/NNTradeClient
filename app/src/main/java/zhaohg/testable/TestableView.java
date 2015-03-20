package zhaohg.testable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class TestableView extends View implements Testable {

    private OnTestFinishedListener testFinishedListener;

    public TestableView(Context context) {
        super(context);
    }

    public TestableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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
