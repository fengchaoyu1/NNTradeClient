package zhaohg.api.post;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zhaohg.json.JsonObject;

public class Post {

    public static final int POST_TYPE_SELL = 0;
    public static final int POST_TYPE_BUY = 1;
    public static final int POST_TYPE_CHAT = 2;

    private int type;
    private String title;
    private String description;
    private String userId;
    private String userName;
    private String postId;
    private boolean isOpen;
    private String imageSetId;
    private String commentsId;
    private Date postDate;
    private Date modifyDate;

    public Post() {
        this.type = POST_TYPE_SELL;
        this.title = "";
        this.description = "";
        this.userId = "";
        this.userName = "";
        this.imageSetId = "";
        this.commentsId = "";
        this.postDate = new Date();
        this.modifyDate = new Date();
    }

    public Post(JsonObject postObject) {
        this.postId = postObject.getValue("post_id").getString();
        this.type = postObject.getValue("type").getInteger();
        this.title = postObject.getValue("title").getString();
        this.description = postObject.getValue("description").getString();
        this.userId = postObject.getValue("user_id").getString();
        this.userName = postObject.getValue("user_name").getString();
        this.imageSetId = postObject.getValue("image_set_id").getString();
        this.commentsId = postObject.getValue("comments_id").getString();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            this.postDate = dateFormat.parse(postObject.getValue("post_date").getString());
            this.modifyDate = dateFormat.parse(postObject.getValue("modify_date").getString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.isOpen = postObject.getValue("is_open").getBoolean();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getImageSetId() {
        return imageSetId;
    }

    public void setImageSetId(String imageSetId) {
        this.imageSetId = imageSetId;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPostDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.postDate);
    }

    public String getModifyDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.modifyDate);
    }

}
