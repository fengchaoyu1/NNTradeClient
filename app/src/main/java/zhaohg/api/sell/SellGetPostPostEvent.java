package zhaohg.api.sell;

public interface SellGetPostPostEvent {
    public void onSuccess(SellPost post);
    public void onFailure(int errno);
}
