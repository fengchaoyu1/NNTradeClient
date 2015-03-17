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

public class AccountRegister extends ApiBase {

    public static String RESOURCE_URL = "account/user/";

    private String username = "";
    private String password = "";

    private AccountRegisterPostEvent event;

    public AccountRegister(Context context) {
        super(context);
    }

    public void setParameter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setEvent(AccountRegisterPostEvent event) {
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
                            event.onSuccess();
                        }
                    }
                }
            }
        });
        this.task.execute();
    }

}
