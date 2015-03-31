package zhaohg.api.post;

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

public class GetPostList extends ApiBase {

    public static String RESOURCE_URL = "post/all/";

    private GetPostListPostEvent event;

    private int pageNum;

    public GetPostList(Context context) {
        super(context);
    }

    public void setParameter(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setEvent(GetPostListPostEvent event) {
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
                            List<Post> posts = new ArrayList<>();
                            JsonArray postArray = values.getValue("posts").getJsonArray();
                            for (int i = 0; i < postArray.size(); ++i) {
                                JsonObject postObject = postArray.get(i).getJsonObject();
                                Post post = new Post(postObject);
                                posts.add(post);
                            }
                            event.onSuccess(posts, values.getValue("is_end").getBoolean());
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
