package com.tuhanbao.base.util.config;

public class BaseConfigConstants
{
    //debug
    public static final String PATTERN = "pattern";

    public static final String ALL_PATTERN = "all_pattern";

    public static final String IS_MAINTAINING = "is_maintaining";

    public static final String WHITE_LIST = "white_list";

    public static final String REPAIR_TIME = "REPAIR_TIME";
    
    public static final String PASSWORD = "password";
    
    //TCP端口
    public static final String TCP_PORT = "tcpport";
    
    //mo在缓存中的过期时间
    public static final String MO_OVERDUE_MIN = "mo_overdue_min";

    //http端口
    public static final String HTTP_PORT = "httpport";
    
    //读buffer大小
    public static final String BUFFER_READ_SIZE = "buffer_read_size";
    
    //最大读buffer大小
    public static final String MAX_BUFFER_READ_SIZE = "max_buffer_read_size";

    //最大缓存临时session数
    public static final String MAX_TEMP_SESSION_SIZE = "max_temp_session_size";

    //最大在线session数
    public static final String MAX_ONLINE_SESSION_SIZE = "max_online_session_size";

    //resultset type设置
    public static final String RESULTSET_TYPE = "resultset_type";

    //tcp过期时长
    public static final String TCP_IDLE_TIME = "tcp_idle_time";
    
    //tcp登陆过期时长，在建立session，一定时间内未发起登陆命令，会关闭连接
    public static final String TCP_LOGIN_TIME = "tcp_login_time";

    //http过期时长
    public static final String HTTP_IDLE_TIME = "http_idle_time";

    public static final String SERVER_VERSION = "version";
    
    //时间比例
    public static final String TIME_SPEED = "time_speed";
    
    //db常量
    public static final String DB_DRIVER = "db_driver";
    public static final String DB_URL = "db_url";
    public static final String DB_IP = "db_ip";
    public static final String DB_PORT = "db_port";
    public static final String DB_INSTANCE = "db_instance";
    public static final String DB_USER = "db_user";
    public static final String DB_PASSWORD = "db_password";
    public static final String DB_USEUNICODE = "db_useUnicode";
    public static final String DB_ENCODE = "db_encode";
    public static final String DB_MIN_CONN_NUM = "db_min_conn_num";
    public static final String DB_MAX_CONN_NUM = "db_max_conn_num";
    public static final String DB_OVERDUE_TIME = "db_overdue_time";
    public static final String DB_BACKUP_URL = "db_backup_url";
    public static final String DB_BACKUP_TIME = "db_backup_time";
    public static final String AUTO_CLEAR_CONN = "auto_clear_conn";


    //线程配置
    public static final String SCHEDULER_THREAD_NUM = "scheduler_thread_num";
    public static final String MAX_SCHEDULER_THREAD_NUM = "max_scheduler_thread_num";
    public static final String THREAD_NUM = "thread_num";
    public static final String MAX_THREAD_NUM = "max_thread_num";
    
    //凌晨事务启动时间
    public static final String AFFAIR_START_MIN = "affair_start_min";
    
    //中心服务器地址
    public static final String CENTER_IP = "center_ip";
    public static final String CENTER_HTTP_PORT = "center_http_port";
    
    //加解密配置
    public static final String HTTP_ENCLIPHER = "http_enclipher";
    public static final String TCP_ENCLIPHER = "tcp_enclipher";

    // 管理员手机号码
    public static final String ADMIN_MOBILE = "admin_mobile";
}
