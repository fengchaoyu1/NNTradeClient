package zhaohg.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import zhaohg.api.post.GetPostList;
import zhaohg.api.post.GetPostListPostEvent;
import zhaohg.api.post.Post;
import zhaohg.main.R;
import zhaohg.testable.TestableFragment;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public class PostsFragment extends TestableFragment {

    private Context context;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclePosts;
    private ImageView imageNewPost;

    private boolean refresh = false;
    private boolean loading = false;
    private int pageNum = 1;

    public PostsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        this.context = view.getContext();

        this.recyclePosts = (RecyclerView) view.findViewById(R.id.recycle_posts);
        this.layoutManager = new LinearLayoutManager(context);
        this.recyclePosts.setLayoutManager(layoutManager);
        PostsAdapter adapter = new PostsAdapter(context);
        this.recyclePosts.setAdapter(adapter);
        this.recyclePosts.setOnScrollListener(new OnPostsScrollListener());

        this.imageNewPost = (ImageView) view.findViewById(R.id.image_new);
        this.imageNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!refresh) {
                    refresh = true;
                    pageNum = 1;
                    loadNextPage();
                }
            }
        });

        loadNextPage();
        return view;
    }

    public void loadNextPage() {
        loading = true;
        GetPostList getPostList = new GetPostList(context);
        getPostList.setParameter(pageNum);
        getPostList.setEvent(new GetPostListPostEvent() {
            @Override
            public void onSuccess(List<Post> posts) {
                PostsAdapter adapter = (PostsAdapter) recyclePosts.getAdapter();
                if (refresh) {
                    refresh = false;
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.clear();
                }
                adapter.append(posts);
                ++pageNum;
                loading = false;
                finishTest();
            }
            @Override
            public void onFailure(int errno) {
                Toast.makeText(context, context.getString(R.string.sell_get_posts_fail), Toast.LENGTH_SHORT).show();
                loading = false;
                finishTest();
            }
        });
        getPostList.request();
    }

    private class OnPostsScrollListener extends OnScrollListener {

        private int pastVisibleItems;
        private int visibleItemCount;
        private int totalItemCount;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            totalItemCount = layoutManager.getItemCount();
            visibleItemCount = layoutManager.getChildCount();
            pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

            if (!loading) {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loadNextPage();
                }
            }
            if (pastVisibleItems == 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        }

    }

}
