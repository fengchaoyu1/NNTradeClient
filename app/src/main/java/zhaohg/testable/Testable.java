package zhaohg.testable;

public interface Testable {
    public void finishTest();
    public void setOnTestFinishedListener(OnTestFinishedListener listener);
}
