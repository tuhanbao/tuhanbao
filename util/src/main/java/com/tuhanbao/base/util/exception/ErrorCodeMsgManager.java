package com.tuhanbao.base.util.exception;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;

public final class ErrorCodeMsgManager implements ConfigRefreshListener {

    private static final String KEY = "errorCode";
    
    private static Config config = null;

    /**
     * 先简单实现，之后可以通过错误码配置文件实现
     * 
     * @param errCode
     * @return
     */
    public static final String getErrMsg(int errCode, String ... args)
    {
        if (config == null) {
            init();
            
            //初始化后还未空，直接return
            if (config == null) {
                if (errCode == BaseErrorCode.ERROR) {
                    StringBuilder sb = new StringBuilder("system error : no init errorCode.properties");
                    if (args != null) {
                        sb.append("(");
                        for (String arg : args) {
                            sb.append(arg).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        sb.append(")");
                    }
                    return sb.toString();
                }
            }
        }
        String msg = config.getString(errCode + "");
        
        if (args == null) return msg;
        int length = args.length;
        //未用到的参数拼在末尾
        StringBuilder noUseArgs = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String target = "{" + i + "}";
            if (msg.contains(target)) {
                msg = msg.replace(target, args[i]);
            }
            else {
                noUseArgs.append(args[i]).append(Constants.COMMA);
            }
        }
        if (noUseArgs.length() > 0) {
            noUseArgs.deleteCharAt(noUseArgs.length() - 1);
            return msg + "(" + noUseArgs + ")";
        }
        else {
            return msg;
        }
    }
    
    private static final void init() {
        config = ConfigManager.getConfig(KEY);
    }

    @Override
    public void refresh() {
        init();
    }

    @Override
    public String getKey() {
        return KEY;
    }

}
