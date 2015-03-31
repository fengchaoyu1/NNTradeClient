package zhaohg.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class ScrollViewWithBottomEvent extends ScrollView {

    public interface OnHitBottomEvent {
        void onHitBottom();
    }

    OnHitBottomEvent bottomEvent;

    public ScrollViewWithBottomEvent(Context context) {
        super(context);
    }

    public ScrollViewWithBottomEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewWithBottomEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBottomEvent(OnHitBottomEvent bottomEvent) {
        this.bottomEvent = bottomEvent;
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        super.onScrollChanged(left, top, oldLeft, oldTop);
        View view = getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY() + view.getTop()));
        if (diff <= 0){
            if (this.bottomEvent != null) {
                this.bottomEvent.onHitBottom();
            }
        }
    }

}
