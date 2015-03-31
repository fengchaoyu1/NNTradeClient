package zhaohg.api.post;

import java.util.List;

public interface GetPostListPostEvent {
    public void onSuccess(List<Post> posts, boolean isEnd);
    public void onFailure(int errno);
}
