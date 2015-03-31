package zhaohg.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zhaohg.api.comment.Comment;
import zhaohg.api.comment.GetComments;
import zhaohg.api.comment.GetCommentsPostEvent;
import zhaohg.main.R;

public class CommentsView extends LinearLayout {

    private int pageNum = 1;
    private boolean loading = false;
    private String commentsId = "";

    private List<Comment> comments;
    private List<View> commentView;

    public CommentsView(Context context) {
        super(context);
        this.initView();
    }

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    public void initView() {
        this.comments = new ArrayList<>();
        this.commentView = new ArrayList<>();
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    private boolean isExisted(Comment newComment) {
        for (Comment comment : comments) {
            if (comment.getCommentId().equals(newComment.getCommentId())) {
                return true;
            }
        }
        return false;
    }

    private void append(Comment comment) {
        if (!isExisted(comment)) {
            comments.add(comment);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, this, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.textUsername.setText(comment.getUserName());
            viewHolder.textDate.setText(comment.getDateString());
            if (comment.isReply()) {
                viewHolder.textMessage.setText(getContext().getString(R.string.reply_to_) + comment.getUserName() + "\n" + comment.getMessage());
            } else {
                viewHolder.textMessage.setText(comment.getMessage());
            }
            this.addView(view);
        }
    }

    private void append(List<Comment> newComments) {
        for (Comment comment : newComments) {
            append(comment);
        }
    }

    public void loadNextPage() {
        if (!commentsId.isEmpty() && !loading) {
            loading = true;
            GetComments getComments = new GetComments(this.getContext());
            getComments.setParameter(commentsId, pageNum);
            getComments.setEvent(new GetCommentsPostEvent() {
                @Override
                public void onSuccess(List<Comment> comments, boolean isEnd) {
                    append(comments);
                    Log.d("test", "" + comments.size() + " " + isEnd);
                    if (!isEnd) {
                        ++pageNum;
                    }
                    loading = false;
                }

                @Override
                public void onFailure(int errno) {
                    loading = false;
                }
            });
            getComments.request();
        }
    }

    private class ViewHolder {

        public TextView textUsername;
        public TextView textDate;
        public TextView textMessage;

        public ViewHolder(View view) {
            textUsername = (TextView) view.findViewById(R.id.text_username);
            textDate = (TextView) view.findViewById(R.id.text_date);
            textMessage = (TextView) view.findViewById(R.id.text_message);
        }
    }

}
