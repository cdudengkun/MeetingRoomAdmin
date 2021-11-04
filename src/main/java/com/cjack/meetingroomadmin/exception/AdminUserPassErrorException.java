package com.cjack.meetingroomadmin.exception;

import com.cjack.meetingroomadmin.config.ErrorCodeDefine;

/**
 * 用户密码错误
 * Created by root on 5/23/19.
 */
public class AdminUserPassErrorException extends Exception{

    private Integer code = ErrorCodeDefine.USER_PASS_ERROR;
    private String message = ErrorCodeDefine.USER_PASS_ERROR_MSG;

    public AdminUserPassErrorException(){
    }

    public AdminUserPassErrorException(String message){
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
