package zhaohg.api.post;

public interface DeletePostPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
