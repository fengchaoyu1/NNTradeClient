package zhaohg.api.sell;

import java.util.List;

public interface GetSellPostListPostEvent {
    public void onSuccess(List<SellPost> posts);
    public void onFailure(int errno);
}
