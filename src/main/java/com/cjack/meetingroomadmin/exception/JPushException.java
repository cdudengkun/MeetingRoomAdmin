package com.cjack.meetingroomadmin.exception;

import com.cjack.meetingroomadmin.config.ErrorCodeDefine;

public class JPushException extends Exception {
    private Integer code = ErrorCodeDefine.JPUSH_FAILE_ERROR;
    private String message = ErrorCodeDefine.JPUSH_FAILE_ERROR_MSG;

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
