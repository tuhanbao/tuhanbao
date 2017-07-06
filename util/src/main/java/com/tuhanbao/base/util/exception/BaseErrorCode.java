package com.tuhanbao.base.util.exception;

public class BaseErrorCode
{
    public static final int NO_ERROR = 0;

    public static final int ERROR = -1;

    public static final int JOSN_ERROR = 1;

    public static final int HTTP_CODE_NULL = 2;

    public static final int INIT_CONFIGFILE_ERROR = 3;

    public static final int COMMAND_CODE_ISNOT_EXIST = 4;

    public static final int ILLEGAL_ARGUMENT = 5;

    public static final int INDEX_OUTOF_BOUND = 6;
    
    public static final int FILE_NOT_EXISTS = 7;

    public static final int DB_CREATE_CONNECTION_ERROR = 32;

    public static final int DB_CONNECTIONS_FULL = 33;

    public static final int DB_ERROR = 34;

    public static final int DB_COMMIT_ERROR = 35;

    public static final int DB_ROLLBACK_ERROR = 36;

    public static final int MO_NOT_EXIST_WHEN_UPDATE = 37;

    public static final int MO_DELETE_COUNT_ERROR = 38;

    public static final int MO_UPDATE_CONFLICT = 39;

    public static final int ILLEGAL_INCOMING_ARGUMENT = 101;

    public static final int ONLINE_PLAYERS_FULL = 102;

    public static final int SERVER_IS_BUSY = 103;

    public static final int SERVER_IN_AFFAIR = 104;

    public static final int SERVER_IS_MAINTAINING = 105;

    public static final int PLAYER_ISNOT_EXIST = 106;

    public static final int HAVE_NO_RIGHT = 107;

    public static final int RECHARGE_FAIL = 108;

    public static final int VERSION_IS_TOO_OLD = 109;

    public static final int NO_NETWORK = 110;

    public static final int NETWORK_TIMEOUT = 111;

    public static final int EXCUTE_CMD_ERROR = 112;
    
    public static final int SERVER_IS_MAINTAINING_BY_MIN = 113;

    public static final int SERVER_IS_MAINTAINING_BY_HOUR = 114;

    public static final int INVALID_DEVICEID = 201;

    public static final int PASSWORD_INVALID = 202;

    public static final int PASSWORD_WRONG = 203;

    public static final int USER_ISNOT_EXIST = 204;

    public static final int INVALID_ACCOUNT = 205;

    public static final int ACCOUNT_LENGTH_ERROR = 206;

    public static final int ACCOUNT_IS_EXIST = 207;

    public static final int PLEASE_LOGIN_FIRST = 208;

    public static final int GET_VERIFICATION_CODE_ERROR = 209;

    public static final int INVALID_VERIFICATION = 210;

    public static final int WRONG_VERIFICATION = 211;

    public static final int PLAYER_LOGIN_TWICE = 212;

    public static final int AUTHCODE_GET_FAIL = 213;

    public static final int AUTHCODE_VAL_FAIL = 214;
    
    public static final int AUTHCODE_IS_EXPIRE = 215;

    public static final int NO_AUTHCODE = 216;

    public static final int AUTHCODE_SEND_TOO_MANY = 217;

    public static final int RESEND_AUTHCODE = 218;
    
    public static final int NO_RECORD_DATA = 219;

}