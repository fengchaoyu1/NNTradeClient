package zhaohg.sell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import zhaohg.api.sell.GetSellPostList;
import zhaohg.api.sell.GetSellPostListPostEvent;
import zhaohg.api.sell.SellPost;
import zhaohg.main.R;
import zhaohg.testable.TestableFragment;

public class SellPostsFragment extends TestableFragment {

    private Context context;

    private RecyclerView recyclePosts;
    private ImageView imageNewPost;

    private int pageNum = 1;

    public SellPostsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_posts, container, false);
        this.context = view.getContext();
        this.recyclePosts = (RecyclerView) view.findViewById(R.id.recycle_posts);
        this.recyclePosts.setLayoutManager(new LinearLayoutManager(context));
        SellPostsAdapter adapter = new SellPostsAdapter(context);
        this.recyclePosts.setAdapter(adapter);
        this.imageNewPost = (ImageView) view.findViewById(R.id.image_new);
        this.imageNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EditSellPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        loadNextPage();
        return view;
    }

    public void loadNextPage() {
        GetSellPostList getSellPostList = new GetSellPostList(context);
        getSellPostList.setParameter(pageNum);
        getSellPostList.setEvent(new GetSellPostListPostEvent() {
            @Override
            public void onSuccess(List<SellPost> posts) {
                SellPostsAdapter adapter = (SellPostsAdapter) recyclePosts.getAdapter();
                adapter.append(posts);
                ++pageNum;
                finishTest();
            }
            @Override
            public void onFailure(int errno) {
                Toast.makeText(context, context.getString(R.string.sell_get_posts_fail), Toast.LENGTH_SHORT).show();
                finishTest();
            }
        });
        getSellPostList.request();
    }

}
