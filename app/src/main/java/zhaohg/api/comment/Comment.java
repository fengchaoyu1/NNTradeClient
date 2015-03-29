package zhaohg.api.comment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class Comment {

    private String commentId;
    private String userId;
    private String userName;
    private String message;
    private boolean isReply;
    private String replyCommentId;
    private String replyUserId;
    private String replyUserName;
    private Date date;

    public Comment() {
        this.userId = "";
        this.userName = "";
        this.message = "";
        this.isReply = false;
        this.replyCommentId = "";
        this.replyUserId = "";
        this.replyUserName = "";
        this.date = new Date();
    }

    public Comment(JsonObject commentObject) {
        this.commentId = commentObject.getValue("comment_id").getString();
        this.userId = commentObject.getValue("user_id").getString();
        this.userName = commentObject.getValue("user_name").getString();
        this.message = commentObject.getValue("message").getString();
        JsonValue replyValue = commentObject.getValue("reply");
        if (replyValue == null) {
            this.isReply = false;
            this.replyCommentId = "";
            this.replyUserId = "";
            this.replyUserName = "";
        } else {
            JsonObject reply = replyValue.getJsonObject();
            this.isReply = true;
            this.replyCommentId = reply.getValue("comment_id").getString();
            this.replyUserId = reply.getValue("user_id").getString();
            this.replyUserName = reply.getValue("user_name").getString();
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            this.date = dateFormat.parse(commentObject.getValue("date").getString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean isReply) {
        this.isReply = isReply;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }


    public Date getDate() {
        return date;
    }

    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
