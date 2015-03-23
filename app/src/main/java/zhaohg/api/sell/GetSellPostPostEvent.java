package zhaohg.api.sell;

public interface GetSellPostPostEvent {
    public void onSuccess(SellPost post);
    public void onFailure(int errno);
}
