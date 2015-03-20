package zhaohg.testable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TestableLinearLayout extends LinearLayout implements Testable {

    private OnTestFinishedListener testFinishedListener;

    public TestableLinearLayout(Context context) {
        super(context);
    }

    public TestableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
