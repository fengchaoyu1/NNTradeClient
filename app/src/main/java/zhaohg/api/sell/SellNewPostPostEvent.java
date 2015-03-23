package zhaohg.api.sell;

public interface SellNewPostPostEvent {
    public void onSuccess(int postId);
    public void onFailure(int errno);
}
