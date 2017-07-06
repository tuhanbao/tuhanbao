package com.tuhanbao.base.util.gm;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.log.LogManager;

public class RuntimeUtil
{
    /**
     * 本方法会在脚本执行完后才返回，如果是长期运行的脚本，会被阻塞，请勿调用此方法
     * @param sh
     * @return
     * @throws GameSoulException
     */
    public static CMDResult excuteCmd(String sh) throws MyException
    {
        Process process;
        try
        {
            process = Runtime.getRuntime().exec(sh);
            //waitFor方法会阻塞，如果是长期运行的脚本，请勿调用此方法
            int resultCode = process.waitFor();
            CMDResult result = new CMDResult(resultCode);
            LogManager.info("sh " + sh + Constants.COLON + resultCode);
            result.setErrorMsg(IOUtil.read(process.getErrorStream()));
            result.setResult(IOUtil.read(process.getInputStream()));
            return result;
        }
        catch (Exception e)
        {
            throw new MyException(BaseErrorCode.EXCUTE_CMD_ERROR);
        }
    }
}
