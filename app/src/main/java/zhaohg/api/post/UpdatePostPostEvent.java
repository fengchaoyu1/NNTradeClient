package zhaohg.api.post;

public interface UpdatePostPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
