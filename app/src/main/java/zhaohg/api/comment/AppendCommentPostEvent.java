package zhaohg.api.comment;

public interface AppendCommentPostEvent {
    public void onSuccess();
    public void onFailure(int errno);
}
