package zhaohg.api.comment;

import java.util.List;

public interface GetCommentsPostEvent {
    public void onSuccess(List<Comment> comments);
    public void onFailure(int errno);
}
