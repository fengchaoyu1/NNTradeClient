package zhaohg.api.post;

public interface GetPostPostEvent {
    public void onSuccess(Post post);
    public void onFailure(int errno);
}
