package zhaohg.api.account;

import android.content.Context;

import zhaohg.api.ApiBase;
import zhaohg.api.ApiErrno;
import zhaohg.api.Encryption;
import zhaohg.api.PostEvent;
import zhaohg.api.RequestParam;
import zhaohg.api.RequestTask;
import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class AccountLogin extends ApiBase {

    public static String RESOURCE_URL = "account/login/";

    private String username = "";
    private String password = "";

    private AccountLoginPostEvent event;

    public AccountLogin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return BASE_URL + RESOURCE_URL;
    }

    public void setParameter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setEvent(AccountLoginPostEvent event) {
        this.event = event;
    }

    @Override
    public void request() {
        this.task = new RequestTask();
        RequestParam param = new RequestParam();
        param.setUrl(this.getUrl());
        param.setMethod(RequestParam.METHOD_POST);
        param.addParam("username", this.username);
        param.addParam("password", Encryption.md5(this.password));
        this.saveToken("");
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
                            int user_id = values.getValue("user_id").getInteger();
                            saveToken(Encryption.md5(password));
                            saveUsername(username);
                            saveUserId(user_id);
                            event.onSuccess();
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
