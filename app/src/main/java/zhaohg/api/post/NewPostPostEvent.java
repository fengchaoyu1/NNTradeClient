package zhaohg.api.post;

public interface NewPostPostEvent {
    public void onSuccess(String postId);
    public void onFailure(int errno);
}
