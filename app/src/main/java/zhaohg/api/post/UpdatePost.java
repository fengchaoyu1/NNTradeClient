package zhaohg.api.post;

import android.content.Context;

import java.util.List;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class UpdatePost extends ApiBase {

    public static String RESOURCE_URL = "post/";

    private String postId;
    private String title;
    private String description;
    private List<String> imageIdList;
    private boolean isOpen;

    private boolean updateTitle;
    private boolean updateDescription;
    private boolean updateImageIdList;
    private boolean updateIsOpen;

    private UpdatePostPostEvent event;

    public UpdatePost(Context context) {
        super(context);
        this.updateTitle = false;
        this.updateDescription = false;
        this.updateImageIdList = false;
        this.updateIsOpen = false;
    }

    public void setParameter(String postId) {
        this.postId = postId;
    }

    public void setUpdateTitle(String title) {
        this.title = title;
        this.updateTitle = true;
    }

    public void setUpdateDescription(String description) {
        this.description = description;
        this.updateDescription = true;
    }

    public void setUpdateImageIdList(List<String> imageIdList) {
        this.imageIdList = imageIdList;
        this.updateImageIdList = true;
    }

    public void setUpdateOpen(boolean isOpen) {
        this.isOpen = isOpen;
        this.updateIsOpen = true;
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + this.postId + "/";
    }

    public void setParameter(String title, String description, List<String> imageIdList) {
        this.title = title;
        this.description = description;
        this.imageIdList = imageIdList;
    }

    public void setEvent(UpdatePostPostEvent event) {
        this.event = event;
    }

    @Override
    public void request() {
        this.task = new RequestTask();
        RequestParam param = new RequestParam();
        param.setUrl(this.getUrl());
        param.setMethod(RequestParam.METHOD_PUT);
        param.addParam("user_id", this.loadUserId());
        param.setToken(this.loadToken());
        if (this.updateTitle) {
            param.addParam("title", this.title);
        }
        if (this.updateDescription) {
            param.addParam("description", this.description);
        }
        if (this.updateImageIdList) {
            String idList = "";
            for (int i = 0; i < imageIdList.size(); ++i) {
                if (i > 0) {
                    idList += ",";
                }
                idList += imageIdList.get(i);
            }
            param.addParam("image_id_list", idList);
        }
        if (this.updateIsOpen) {
            param.addParam("is_open", this.isOpen ? "true" : "false");
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
