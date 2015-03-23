package zhaohg.api.sell;

public interface SellNewPostPostEvent {
    public void onSuccess(String postId);
    public void onFailure(int errno);
}
