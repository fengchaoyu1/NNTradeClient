package zhaohg.api;

import android.content.Context;

import zhaohg.trade.R;

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

    public static String getErrorMessage(Context context, int errno) {
        switch (errno) {
            case ERRNO_NO_ERROR:
                return context.getString(R.string.ERRNO_NO_ERROR);
            case ERRNO_CONNECTION:
                return context.getString(R.string.ERRNO_CONNECTION);
            case ERRNO_INVALID_REQUEST_METHOD:
                return context.getString(R.string.ERRNO_INVALID_REQUEST_METHOD);
            case ERRNO_MISSING_PARAMETER:
                return context.getString(R.string.ERRNO_MISSING_PARAMETER);

            case ERRNO_USERNAME_EXIST:
                return context.getString(R.string.ERRNO_USERNAME_EXIST);
            case ERRNO_USERNAME_NON_EXIST:
                return context.getString(R.string.ERRNO_USERNAME_NON_EXIST);
            case ERRNO_MISMATCH_USERNAME_PASSWORD:
                return context.getString(R.string.ERRNO_MISMATCH_USERNAME_PASSWORD);
            case ERRNO_NO_TOKEN:
                return context.getString(R.string.ERRNO_NO_TOKEN);
            case ERRNO_MISMATCH_TOKEN:
                return context.getString(R.string.ERRNO_MISMATCH_TOKEN);
        }
        return "";
    }

}
