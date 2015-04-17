package zhaohg.api.post;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class GetPost extends ApiBase {

    public static String RESOURCE_URL = "post/";

    private GetPostPostEvent event;

    private String postId;

    public GetPost(Context context) {
        super(context);
    }

    public void setParameter(String postId) {
        this.postId = postId;
    }

    public void setEvent(GetPostPostEvent event) {
        this.event = event;
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + postId + "/";
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
                            JsonObject postObject = values.getValue("post").getJsonObject();
                            Post post = new Post(postObject);
                            event.onSuccess(post);
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
