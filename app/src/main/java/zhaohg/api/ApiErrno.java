package zhaohg.api;

import android.content.Context;

import zhaohg.main.R;

public class ApiErrno {

    public static final int ERRNO_NO_ERROR = 0;
    public static final int ERRNO_CONNECTION = -1;
    public static final int ERRNO_INVALID_REQUEST_METHOD = -2;
    public static final int ERRNO_MISSING_PARAMETER = -3;

    public static final int ERRNO_USERNAME_EXIST = -1000;
    public static final int ERRNO_USERNAME_NON_EXIST = -1001;
    public static final int ERRNO_MISMATCH_USERNAME_PASSWORD = -1002;
    public static final int ERRNO_NO_TOKEN = -1003;
    public static final int ERRNO_MISMATCH_TOKEN = -1004;

    public static final int ERRNO_NOT_EXIST = -2000;
    public static final int ERRNO_NOT_OWNER = -2001;

    public static String getErrorMessage(Context context, int errno) {
        switch (errno) {
            case ERRNO_NO_ERROR:
                return context.getString(R.string.errno_no_error);
            case ERRNO_CONNECTION:
                return context.getString(R.string.errno_connection);
            case ERRNO_INVALID_REQUEST_METHOD:
                return context.getString(R.string.errno_invalid_request_method);
            case ERRNO_MISSING_PARAMETER:
                return context.getString(R.string.errno_missing_parameter);

            case ERRNO_USERNAME_EXIST:
                return context.getString(R.string.errno_username_exist);
            case ERRNO_USERNAME_NON_EXIST:
                return context.getString(R.string.errno_username_non_exist);
            case ERRNO_MISMATCH_USERNAME_PASSWORD:
                return context.getString(R.string.errno_mismatch_username_password);
            case ERRNO_NO_TOKEN:
                return context.getString(R.string.errno_no_token);
            case ERRNO_MISMATCH_TOKEN:
                return context.getString(R.string.errno_mismatch_token);

            case ERRNO_NOT_EXIST:
                return context.getString(R.string.errno_not_exist);
            case ERRNO_NOT_OWNER:
                return context.getString(R.string.errno_not_owner);
        }
        return "";
    }

}
