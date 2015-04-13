package zhaohg.api.image;

import java.util.List;

public interface GetImageSetPostEvent {
    public void onSuccess(List<Image> images);
    public void onFailure(int errno);
}
