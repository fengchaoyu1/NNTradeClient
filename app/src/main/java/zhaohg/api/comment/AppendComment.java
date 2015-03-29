package zhaohg.api.comment;

import android.content.Context;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class AppendComment extends ApiBase {

    public static String RESOURCE_URL = "comment/";

    private String commentsId;
    private String message;
    private int reply;

    private AppendCommentPostEvent event;

    public AppendComment(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + commentsId + "/";
    }

    public void setParameter(String commentsId, String message) {
        this.setParameter(commentsId, message, 0);
    }

    public void setParameter(String commentsId, String message, int reply) {
        this.commentsId = commentsId;
        this.message = message;
        this.reply = reply;
    }

    public void setEvent(AppendCommentPostEvent event) {
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
        param.addParam("message", message);
        if (this.reply > 0) {
            param.addParam("reply", reply);
        }
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