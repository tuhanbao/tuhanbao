package com.tuhanbao.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.web.controller.helper.ResultBean;

/**
 * 异常处理器
 * Created by dell on 2016/6/16.
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    public @ResponseBody ResultBean handlerException(Exception ex, HttpServletRequest request){
    	ResultBean msg = new ResultBean();
        MyException e = MyException.getMyException(ex);
        msg.setMsg(e.getMessage());
        msg.setCode(e.getErrCode());
        msg.setData(Constants.EMPTY);
        LogManager.info("result:" + msg.toString());
        return msg;
    }

}
