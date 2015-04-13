package zhaohg.api.image;

import android.content.Context;

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

public class GetImageSet extends ApiBase {

    public static String RESOURCE_URL = "image/set/";

    private GetImageSetPostEvent event;

    private String setId;

    public GetImageSet(Context context) {
        super(context);
    }

    public void setParameter(String setId) {
        this.setId = setId;
    }

    public void setEvent(GetImageSetPostEvent event) {
        this.event = event;
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + setId + "/";
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
                            List<Image> images = new ArrayList<>();
                            JsonArray postArray = values.getValue("images").getJsonArray();
                            for (int i = 0; i < postArray.size(); ++i) {
                                JsonObject postObject = postArray.get(i).getJsonObject();
                                Image image = new Image(postObject);
                                images.add(image);
                            }
                            event.onSuccess(images);
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
