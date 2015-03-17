package zhaohg.api;

import android.content.Context;

import zhaohg.json.JsonObject;
import zhaohg.json.JsonValue;

public class AccountLogin extends ApiBase {

    public static String RESOURCE_URL = "account/token/";

    private String username = "";
    private String password = "";

    private AccountLoginPostEvent event;

    public AccountLogin(Context context) {
        super(context);
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
        param.setUrl(BASE_URL + RESOURCE_URL);
        param.setMethod(RequestParam.METHOD_POST);
        param.addParam("username", this.username);
        param.addParam("password", Encryption.md5(this.password));
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
                            String token = values.getValue("token").getString();
                            saveToken(token);
                            event.onSuccess();
                        }
                    }
                }
            }
        });
        this.task.execute();
    }
}
