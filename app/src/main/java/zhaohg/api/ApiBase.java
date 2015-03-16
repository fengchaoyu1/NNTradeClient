package zhaohg.api;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class ApiBase {

    public static String BASE_URL = "http://10.0.2.2:8000/";
    public static String SP_NAME = "api";
    public static String SP_KEY_TOKEN = "token";

    protected Context context;
    protected RequestTask task;

    private String token = "";

    public ApiBase(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {
        if (this.context != null) {
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SP_KEY_TOKEN, token);
            editor.commit();
        }
        this.token = token;
    }

    public String loadToken() {
        if (this.token == "") {
            if (this.context != null) {
                SharedPreferences sharedPreferences = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                this.token = sharedPreferences.getString(SP_KEY_TOKEN, "");
            }
        }
        return this.token;
    }

    public void cancel() {
        if (this.task != null) {
            this.task.cancel(true);
        }
    }

    public abstract void request();

}
