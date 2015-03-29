package zhaohg.api.comment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zhaohg.json.JsonObject;

public class Comment {

    private String commentId;
    private String userId;
    private String userName;
    private String message;
    private int reply;
    private Date date;

    public Comment() {
        this.userId = "";
        this.userName = "";
        this.message = "";
        this.reply = 0;
        this.date = new Date();
    }

    public Comment(JsonObject commentObject) {
        this.commentId = commentObject.getValue("comment_id").getString();
        this.userId = commentObject.getValue("user_id").getString();
        this.userName = commentObject.getValue("user_name").getString();
        this.message = commentObject.getValue("message").getString();
        this.reply = commentObject.getValue("reply").getInteger();
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

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public Date getDate() {
        return date;
    }

    public String getPostDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
