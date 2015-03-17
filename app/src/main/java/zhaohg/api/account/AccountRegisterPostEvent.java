package zhaohg.api.account;

public interface AccountRegisterPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
