package zhaohg.api.sell;

import android.content.Context;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class DeleteSellPost extends ApiBase {

    public static String RESOURCE_URL = "sell/post/";

    private DeleteSellPostPostEvent event;

    private String postId;

    public DeleteSellPost(Context context) {
        super(context);
    }

    public void setParameter(String postId) {
        this.postId = postId;
    }

    public void setEvent(DeleteSellPostPostEvent event) {
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
        param.setMethod(RequestParam.METHOD_DELETE);
        param.addParam("user_id", this.loadUserId());
        param.setToken(this.loadToken());
        param.addParam("postId", postId);
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
                            event.onSuccess();
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
