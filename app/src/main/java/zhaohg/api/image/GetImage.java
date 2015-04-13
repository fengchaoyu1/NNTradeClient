package zhaohg.api.image;

import android.content.Context;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class GetImage extends ApiBase {

    public static String RESOURCE_URL = "image/";

    private GetImagePostEvent event;

    private String imageId;

    public GetImage(Context context) {
        super(context);
    }

    public void setParameter(String imageId) {
        this.imageId = imageId;
    }

    public void setEvent(GetImagePostEvent event) {
        this.event = event;
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + imageId + "/";
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
                            JsonObject imageObject = values.getValue("image").getJsonObject();
                            Image post = new Image(imageObject);
                            event.onSuccess(post);
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
