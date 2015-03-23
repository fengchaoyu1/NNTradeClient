package zhaohg.api.sell;

public interface NewSellPostPostEvent {
    public void onSuccess(String postId);
    public void onFailure(int errno);
}
