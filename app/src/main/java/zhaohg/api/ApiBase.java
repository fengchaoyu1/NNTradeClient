package zhaohg.api;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class ApiBase {

    public static final String BASE_URL = "http://10.0.2.2:8000/";
    public static final String SP_NAME = "api";
    public static final String SP_KEY_TOKEN = "token";
    public static final String SP_KEY_USERNAME = "username";

    protected Context context;
    protected RequestTask task;

    private static String token = "";
    private static String username = "";

    public ApiBase(Context context) {
        this.context = context;
    }

    private void saveValue(String key, String value) {
        if (this.context != null) {
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    private String loadStringValue(String key) {
        if (this.context != null) {
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void saveToken(String value) {
        this.saveValue(SP_KEY_TOKEN, value);
        token = value;
    }

    public String loadToken() {
        if (token == "") {
            token = loadStringValue(SP_KEY_TOKEN);
        }
        return token;
    }

    public void saveUsername(String value) {
        this.saveValue(SP_KEY_USERNAME, value);
        username = value;
    }

    public String loadUsername() {
        if (username == "") {
            username = loadStringValue(SP_KEY_USERNAME);
        }
        return username;
    }

    public boolean hasLogin() {
        return this.loadToken() != "";
    }

    public void cancel() {
        if (this.task != null) {
            this.task.cancel(true);
        }
    }

    public abstract void request();

}
