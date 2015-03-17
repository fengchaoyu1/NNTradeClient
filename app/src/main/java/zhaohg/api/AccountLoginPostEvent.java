package zhaohg.api;

public interface AccountLoginPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
