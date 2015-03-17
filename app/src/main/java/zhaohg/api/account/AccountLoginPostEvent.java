package zhaohg.api.account;

public interface AccountLoginPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
