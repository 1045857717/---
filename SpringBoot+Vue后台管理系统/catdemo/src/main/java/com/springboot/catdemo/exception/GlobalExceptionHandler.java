package com.springboot.catdemo.exception;

import com.springboot.catdemo.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理类
 * @Author: can
 * @Description:
 * @Date: Create in 23:49 2022/3/12
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果抛出异常的是ServiceException，则调用该方法
     * @param se 业务异常
     * @return Result
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }
}
