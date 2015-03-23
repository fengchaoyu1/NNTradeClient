package zhaohg.api.sell;

import android.content.Context;

import java.util.List;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class SellNewPost extends ApiBase {

    public static String RESOURCE_URL = "sell/post/";

    private String title;
    private String description;
    private List<String> imageIdList;

    private SellNewPostPostEvent event;

    public SellNewPost(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL;
    }

    public void setParameter(String title, String description, List<String> imageIdList) {
        this.title = title;
        this.description = description;
        this.imageIdList = imageIdList;
    }

    public void setEvent(SellNewPostPostEvent event) {
        this.event = event;
    }

    @Override
    public void request() {
        this.task = new RequestTask();
        RequestParam param = new RequestParam();
        param.setUrl(this.getUrl());
        param.setMethod(RequestParam.METHOD_POST);
        param.addParam("user_id", this.loadUserId());
        param.setToken(this.loadToken());
        param.addParam("title", this.title);
        param.addParam("description", this.description);
        String idList = "";
        for (int i = 0; i < imageIdList.size(); ++i) {
            if (i > 0) {
                idList += ",";
            }
            idList += imageIdList.get(i);
        }
        param.addParam("image_id_list", idList);
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
                            int postId = values.getValue("post_id").getInteger();
                            event.onSuccess(postId);
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
