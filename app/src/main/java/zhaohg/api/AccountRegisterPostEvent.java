package zhaohg.api;

public interface AccountRegisterPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
