package com.cjack.meetingroomadmin.exception;

import com.cjack.meetingroomadmin.config.ErrorCodeDefine;

/**
 * 单词不存在
 * Created by root on 5/23/19.
 */
public class WordNotFoundException extends Exception{

    private Integer code = ErrorCodeDefine.DICTIONARY_WORD_NOT_FOUND_ERROR;
    private String message = ErrorCodeDefine.DICTIONARY_WORD_NOT_FOUND_MSG;

    public WordNotFoundException(){
    }

    public WordNotFoundException(String message){
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
