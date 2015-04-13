package zhaohg.api.image;

public interface GetImagePostEvent {
    public void onSuccess(Image image);
    public void onFailure(int errno);
}
