package zhaohg.api.sell;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonArray;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class GetSellPostList extends ApiBase {
    public static String RESOURCE_URL = "sell/posts/";

    private GetSellPostListPostEvent event;

    private int pageNum;

    public GetSellPostList(Context context) {
        super(context);
    }

    public void setParameter(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setEvent(GetSellPostListPostEvent event) {
        this.event = event;
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + pageNum + "/";
    }

    @Override
    public void request() {
        this.task = new RequestTask();
        RequestParam param = new RequestParam();
        param.setUrl(this.getUrl());
        param.setMethod(RequestParam.METHOD_GET);
        this.task.setRequestParam(param);
        this.task.setRequestPostEvent(new PostEvent() {
            @Override
            public void onPostEvent(JsonValue json) {
                if (event != null) {
                    if (json == null) {
                        event.onFailure(ApiErrno.ERRNO_CONNECTION);
                    } else {
                        JsonObject values = json.getJsonObject();
                        if (!values.getValue("success").getBoolean()) {
                            event.onFailure(values.getValue("errno").getInteger());
                        } else {
                            List<SellPost> posts = new ArrayList<>();
                            JsonArray postArray = values.getValue("posts").getJsonArray();
                            for (int i = 0; i < postArray.size(); ++i) {
                                JsonObject postObject = postArray.get(i).getJsonObject();
                                SellPost post = new SellPost();
                                post.setPostId(postObject.getValue("post_id").getString());
                                post.setTitle(postObject.getValue("title").getString());
                                post.setDescription(postObject.getValue("description").getString());
                                post.setUserId(postObject.getValue("user_id").getString());
                                post.setImageSetId(postObject.getValue("image_set_id").getString());
                                try {
                                    DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                                    post.setPostDate(dateFormat.parse(postObject.getValue("post_date").getString()));
                                    post.setModifyDate(dateFormat.parse(postObject.getValue("modify_date").getString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                post.setOpen(postObject.getValue("is_open").getBoolean());
                                posts.add(post);
                            }
                            event.onSuccess(posts);
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
