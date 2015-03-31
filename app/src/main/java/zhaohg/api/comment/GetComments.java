package zhaohg.api.comment;

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

public class GetComments extends ApiBase {

    public static String RESOURCE_URL = "comment/";

    private GetCommentsPostEvent event;

    private String commentsId;
    private int pageNum;

    public GetComments(Context context) {
        super(context);
    }

    public void setParameter(String commentsId, int pageNum) {
        this.commentsId = commentsId;
        this.pageNum = pageNum;
    }

    public void setEvent(GetCommentsPostEvent event) {
        this.event = event;
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL + commentsId + "/" + pageNum + "/";
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
                            List<Comment> comments = new ArrayList<>();
                            JsonArray commentsArray = values.getValue("comments").getJsonArray();
                            for (int i = 0; i < commentsArray.size(); ++i) {
                                JsonObject commentObject = commentsArray.get(i).getJsonObject();
                                Comment comment = new Comment(commentObject);
                                comments.add(comment);
                            }
                            event.onSuccess(comments, values.getValue("is_end").getBoolean());
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
