package com.cjack.meetingroomadmin.exception;

import com.cjack.meetingroomadmin.config.ErrorCodeDefine;

/**
 * Created by root on 6/23/19.
 */
public class CommonException extends Exception{

    private Integer code = ErrorCodeDefine.INTERFACE_COMMON_ERROR;
    private String message = ErrorCodeDefine.INTERFACE_COMMON_ERROR_MSG;

    public CommonException(){
    }

    public CommonException( String message){
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
