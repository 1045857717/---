package com.springboot.catdemo.exception;

import lombok.Getter;

/**
 * 自定义异常
 * @Author: can
 * @Description:
 * @Date: Create in 23:51 2022/3/12
 */
@Getter
public class ServiceException extends  RuntimeException{

    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
