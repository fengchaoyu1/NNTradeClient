package zhaohg.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhaohg.api.comment.Comment;
import zhaohg.main.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context) {
        this.context = context;
        this.comments = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Comment comment = comments.get(i);
        viewHolder.textUsername.setText(comment.getUserName());
        viewHolder.textDate.setText(comment.getDateString());
        if (comment.isReply()) {
            viewHolder.textMessage.setText(context.getString(R.string.reply_to_) + comment.getUserName() + "\n" + comment.getMessage());
        } else {
            viewHolder.textMessage.setText(comment.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void append(Comment comment) {
        int pos = comments.size();
        comments.add(comment);
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos, 1);
    }

    public void append(List<Comment> newComments) {
        for (Comment comment : newComments) {
            append(comment);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textUsername;
        public TextView textDate;
        public TextView textMessage;

        public ViewHolder(View view) {
            super(view);
            textUsername = (TextView) view.findViewById(R.id.text_username);
            textDate = (TextView) view.findViewById(R.id.text_date);
            textMessage = (TextView) view.findViewById(R.id.text_message);
        }
    }
}