package com.springboot.catdemo.common;


/**
 * @Author: can
 * @Description:
 * @Date: Create in 3:17 2022/6/9
 */
public class PageResult {

    protected String code;

    protected String msg;
    /**
     * 查询数据列表
     */
    protected Object data;

    /**
     * 总数
     */
    protected long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;
    /**
     * 当前页
     */
    protected long current = 1;

    public PageResult() {
    }

    public PageResult(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public PageResult(String code, String msg, Object data, long total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }


    public static PageResult success() {
        return new PageResult(Constants.CODE_200, "", null);
    }

    /**
     * code:200,msg:"",data:data
     * @param data 返回的data
     * @return
     */
    public static PageResult success(Object data) {
        return new PageResult(Constants.CODE_200, "", data);
    }

    /**
     * code:200,msg:"",data:data
     * @param data 返回的data
     * @param total 返回结果集的总数
     * @return
     */
    public static PageResult success(Object data, long total) {
        return new PageResult(Constants.CODE_200, "", data, total);
    }

    /**
     * code500
     * @return code:500,msg:系统错误,data:null
     */
    public static PageResult error() {
        return new PageResult(Constants.CODE_500, "系统错误", null);
    }

    public static PageResult error(String code, String msg) {
        return new PageResult(code, msg, null);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
