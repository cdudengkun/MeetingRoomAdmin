package com.cjack.meetingroomadmin.response;

/**
 * Created by root on 4/21/19.
 */
public class RestfulResponse {
    private final static Integer DEFAULT_SUCCESS_CODE = 200;
    private final static Integer DEFAULT_ERROR_CODE = 500;
    private final static String DEFAULT_ERROR_MESSAGE = "服务器出现异常，请稍后再试";

    private Integer code;
    private String message;
    private Object data;

    public RestfulResponse( Integer code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RestfulResponse(){

    }

    public static RestfulResponse RETURN( Integer code, String message, Object data){
        return new RestfulResponse( code, message, data);
    }

    public static RestfulResponse SUCCESS( Object data){
        return new RestfulResponse( DEFAULT_SUCCESS_CODE, null, data);
    }

    public static RestfulResponse SUCCESS(){
        return new RestfulResponse( DEFAULT_SUCCESS_CODE, null, null);
    }

    public static RestfulResponse ERROR( String message){
        return new RestfulResponse( DEFAULT_ERROR_CODE, message, null);
    }

    public static RestfulResponse ERROR( Integer code, String message){
        return new RestfulResponse( code, message, null);
    }

    public static RestfulResponse ERROR(){
        return new RestfulResponse( DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE, null);
    }
}
