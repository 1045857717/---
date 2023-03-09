package com.springboot.catdemo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 * @Author: can
 * @Description:
 * @Date: Create in 23:26 2022/3/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    /**
     * code:200,msg:"",data:data
     * @param data 返回的data
     * @return
     */
    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    /**
     * code500
     * @return code:500,msg:系统错误,data:null
     */
    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

}
