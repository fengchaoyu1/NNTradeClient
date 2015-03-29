package zhaohg.post;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhaohg.api.post.Post;
import zhaohg.main.R;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<Post> posts;

    private Context context;

    public PostsAdapter(Context context) {
        this.context = context;
        this.posts = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.setPostId(post.getPostId());
        viewHolder.textTitle.setText(post.getTitle());
        viewHolder.textDescription.setText(post.getDescription());
        viewHolder.textDate.setText(context.getString(R.string.publish_date_) + post.getPostDateString());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private boolean isExisted(Post newPost) {
        for (Post post : posts) {
            if (post.getPostId().equals(newPost.getPostId())) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void append(Post post) {
        if (!isExisted(post)) {
            int pos = posts.size();
            posts.add(post);
            notifyItemInserted(pos);
            notifyItemRangeChanged(pos, 1);
        }
    }

    public void append(List<Post> posts) {
        for (Post post : posts) {
            append(post);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textTitle;
        public TextView textDescription;
        public TextView textDate;

        private String postId;

        public ViewHolder(View view) {
            super(view);
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textDescription = (TextView) view.findViewById(R.id.text_description);
            textDate = (TextView) view.findViewById(R.id.text_publish_date);
            view.setOnClickListener(new OnPostClickerListener());
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        private class OnPostClickerListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra(PostActivity.EXTRA_POST_ID, postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
