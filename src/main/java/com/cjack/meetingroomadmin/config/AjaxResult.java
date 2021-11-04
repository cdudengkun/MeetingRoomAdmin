package com.cjack.meetingroomadmin.config;

import com.cjack.meetingroomadmin.exception.CommonException;

/**
 * Created by root on 5/26/19.
 */
public class AjaxResult {

    private final static Integer DEFAULT_SUCCESS_CODE = 200;
    private final static String DEFAULT_SUCCESS_MSG = "请求成功";

    public AjaxResult(Integer code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Integer code;
    private String msg;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static AjaxResult SUCCESS(){
        return new AjaxResult( DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, null);
    }

    public static AjaxResult SUCCESS( String msg){
        return new AjaxResult( DEFAULT_SUCCESS_CODE, msg, null);
    }

    public static AjaxResult SUCCESS( Object data){
        return new AjaxResult( DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, data);
    }

    public static AjaxResult ERROR( Integer code, String msg){
        return new AjaxResult( code, msg, null);
    }

    /**
     * 返回系统错误
     * @return
     */
    public static AjaxResult ERROR(){
        CommonException exception = new CommonException();
        return new AjaxResult( exception.getCode(), exception.getMessage(), null);
    }
}
