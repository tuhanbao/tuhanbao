package com.tuhanbao.base;

import com.tuhanbao.base.util.objutil.FileUtil;

public class Constants
{
    public static final int TRY_AGAIN_TIME = 3;

    public static final int TIMEOUT = 20;

    public static final int SUCCESS_CODE = 0;

    public static final int FAIL_CODE = -1;

    public static final String SUCCESS_CODE_STR = "0";

    public static final String FAIL_CODE_STR = "-1";

    public static final String UTF_8 = "utf-8";
    public static final String GBK = "GBK";
    public static final String GB2312 = "gb2312";
    public static final String ISO = "ISO-8859-1";

    // 精确度
    public static final int PRECISION = 2;

    // public----------------------------------------
    // 文件名分割符 "/"是通用的，如果根据系统区分分隔符，某些特殊情况会出问题，比如在windows的服务器向linux系统写文件
    public static final String FILE_SEP = "/";
    public static final String MS = "ms";

    public static final String TRUE_STR_VALUE = "1";

    public static final String FALSE_STR_VALUE = "0";

    public static final String ZERO = "0";

    // 常用
    public static final String BLANK = " ";
    public static final String EMPTY = "";
    public static final String ENTER = "\n";
    public static final String STAR = "*";
    public static final String AT = "@";
    public static final String DOLLAR = "$";
    public static final String TAB_SPACE = "    ";
    public static final String TAB = "\t";

    // 标点
    public static final String COMMA = ",";
    public static final String STOP_EN = ".";
    public static final String STOP_ZH = "。";
    public static final String QUOTE = "\"";
    public static final String WELL = "#";
    public static final String QUESTION_MARK = "?";
    public static final String AND_MARK = "&";

    /**
     * 封号“;”
     */
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";
    public static final String EXCLAMATION = "!";
    public static final String EQUAL = "=";
    public static final String SINGLE_QUOTA = "'";
    public static final String UNDER_LINE = "_";
    public static final String MID_LINE = "-";
    public static final String ELLIPSIS = "...";

    // 括号
    public static final String LEFT_PARENTHESE = "(";
    public static final String RIGHT_PARENTHESE = ")";
    public static final char LEFT_PARENTHESE_CHAR = '(';
    public static final char RIGHT_PARENTHESE_CHAR = ')';
    public static final String LEFT_BRACKET = "[";
    public static final String RIGHT_BRACKET = "]";
    public static final String LEFT_BRACE = "{";
    public static final String RIGHT_BRACE = "}";
    public static final String PERCENT_SIGN = "%";

    public static final String MAIL_REGEX = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";

    public static final String STAND_REGEX = "^[a-zA-Z_0-9]+$";

    // 不得包含 !, |, &, =, \, ., *
    public static final String SPECIAL_CHARACTERS_REGEX = "^[^!|&=\\\\.*]+$";

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * 常量字符串
     */
    // 消息处理器
    public static final String MESSAGE_HANDLER = "messageHandler";

    // 命令码
    public static final String CMD = "cmd";

    // 流水号
    public static final String SN = "sn";

    // 数据
    public static final String DATA = "data";

    // 状态 表示错误码
    public static final String STATE = "state";

    // session保存独享
    public static final Object SAVE_SESSION_OBJECT = "saveSessionObject";

    // 错误码信息参数
    public static final String ERROR_ARGS = "args";

    // 配置文件目录
    public static final String CONFIG_URL = "init/config/";
    public static String CONFIG_ROOT = FileUtil.appendPath(System.getProperty("user.dir"), CONFIG_URL);

    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String USE_UNICODE = "useUnicode";
    public static final String CHARACTER_ENCODING = "characterEncoding";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IP = "ip";
    
    public static final String TCP_PORT = "tcp_port";
    public static final String HTTP_PORT = "http_port";
    public static final String DATA_SERVER_IP = "data_server_ip";
    public static final String DATA_SERVER_PORT = "data_server_port";
    public static final String ONLINE_THRESHOLD = "online_threshold";
    public static final String VERSION = "version";
    public static final String ISNEW = "isnew";
    public static final String DOWNLOAD_URL = "download_url";
    


    //常用的基础命令
    //发送错误消息
    public static final int ERROR = -1;
    //心跳
    public static final int HEAT_BEAT = 1;
    //登录
    public static final int LOGIN = 103;
    //debug
    public static final int DEBUG = 10001;
    //停止服务器
    public static final int STOP_SERVER = 10002;
    //重启服务器
    public static final int RESTART_SERVER = 10003;
    //刷新服务器列表
    public static final int REFRESH_SERVER_LIST = 10004;
    //刷新配置文件
    public static final int REFRESH_CONFIG = 10005;
    //生成数据
    public static final int CREATE_DATA_XLS = 10006; 
	
	public static final String STATIC = "static";
	public static final String ABSTRACT = "abstract";
    public static final String FINAL = "final";
    public static final String PACKAGE = "package";
    public static final String INTERFACE = "interface";
	public static final String THROWS = "throws";
	
	public static final String IMPORT = "import";
    public static final String PUBLIC_CLASS = "public class";
    public static final Object PUBLIC_FINAL_CLASS = "public final class";
    public static final String VOID = "void";
    
    public static final String START = "/**";
    public static final String MID = " * ";
    public static final String END = " */";
    
    public static final String PROPERTIES_SUFFIX = ".properties";
    
    //注释符号
    public static final char POUND_SIGN = '#';
    
    public static final String[] CONSTANT_CLASS_NOTES = new String[]{
            "本来为自动生成的常量类",
            "请勿擅自修改",
            "",
            "@tuhanbao"
    };
    

    public static final String GAP1 = "    ";
    public static final String GAP2 = GAP1 + GAP1;
    public static final String GAP3 = GAP2 + GAP1;
    public static final String GAP4 = GAP3 + GAP1;
    public static final String GAP5 = GAP4 + GAP1;
    
    //0为只写服务端，1为只写客户端，2为全写，默认为2
    public static final int SERVER = 0, CLIENT = 1, ALL = 2;
    
    public static final String DEFAULT_PACKAGE_HEAD = "com.tuhanbao";
    
}
