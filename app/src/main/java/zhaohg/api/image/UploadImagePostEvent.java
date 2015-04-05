package zhaohg.api.image;

public interface UploadImagePostEvent {
    public void onSuccess(String imageId);
    public void onFailure(int errno);
}
