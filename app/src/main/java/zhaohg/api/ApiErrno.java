package zhaohg.api;

public class ApiErrno {

    public static final int ERRNO_CONNECTION = -1;
    public static final int ERRNO_INVALID_REQUEST_METHOD = -2;
    public static final int ERRNO_MISSING_PARAMETER = -3;

    public static final int ERRNO_USERNAME_EXIST = -1000;
    public static final int ERRNO_USERNAME_NON_EXIST = -1001;
    public static final int ERRNO_MISMATCH_USERNAME_PASSWORD = -1002;
    public static final int ERRNO_NO_TOKEN = -1003;
    public static final int ERRNO_MISMATCH_TOKEN = -1004;

}
