package com.cjack.meetingroomadmin.exception;

import com.cjack.meetingroomadmin.config.ErrorCodeDefine;

/**
 * 找不到该用户
 * Created by root on 5/23/19.
 */
public class AdminUserNotFoundException extends Exception{

    private Integer code = ErrorCodeDefine.USER_NOT_FOUND;
    private String message = ErrorCodeDefine.USER_NOT_FOUND_MSG;

    public AdminUserNotFoundException(){
    }

    public AdminUserNotFoundException(String message){
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
