package zhaohg.api.image;

import android.content.Context;

import java.io.File;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class UploadImage extends ApiBase {

    public static String RESOURCE_URL = "image/";

    private File imageFile;
    private File thumbnailFile;

    private UploadImagePostEvent event;

    public UploadImage(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL;
    }

    public void setParameter(File imageFile, File thumbnailFile) {
        this.imageFile = imageFile;
        this.thumbnailFile = thumbnailFile;
    }

    public void setEvent(UploadImagePostEvent event) {
        this.event = event;
    }

    @Override
    public void request() {
        this.task = new RequestTask();
        RequestParam param = new RequestParam();
        param.setUrl(this.getUrl());
        param.setMethod(RequestParam.METHOD_FILE);
        param.addParam("user_id", this.loadUserId());
        param.setToken(this.loadToken());
        param.addFile("image", this.imageFile);
        param.addFile("thumbnail", this.thumbnailFile);
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
                            event.onSuccess(values.getValue("image_id").getString());
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
