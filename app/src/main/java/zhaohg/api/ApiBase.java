package zhaohg.api;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class ApiBase {

    public static final String BASE_URL = "http://10.0.2.2:8000/";
    public static final String SP_NAME = "api";
    public static final String SP_KEY_TOKEN = "token";
    public static final String SP_KEY_USERNAME = "username";
    public static final String SP_KEY_USER_ID = "user_id";

    protected Context context;
    protected RequestTask task;

    private static String token = "";
    private static String username = "";
    private static String userId = "";

    public ApiBase(Context context) {
        this.context = context;
    }

    private void saveValue(String key, String value) {
        if (this.context != null) {
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
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
        if (token.equals("")) {
            token = loadStringValue(SP_KEY_TOKEN);
        }
        return token;
    }

    public void saveUsername(String value) {
        this.saveValue(SP_KEY_USERNAME, value);
        username = value;
    }

    public String loadUsername() {
        if (username.equals("")) {
            username = loadStringValue(SP_KEY_USERNAME);
        }
        return username;
    }

    public void saveUserId(int id) {
        userId = "" + id;
        this.saveValue(SP_KEY_USER_ID, userId);
    }

    public String loadUserId() {
        if (userId.equals("")) {
            userId = loadStringValue(SP_KEY_USER_ID);
        }
        return userId;
    }

    public boolean hasLogin() {
        return this.loadToken() != "";
    }

    public void cancel() {
        if (this.task != null) {
            this.task.cancel(true);
        }
    }

    public abstract String getUrl();
    public abstract void request();

}
