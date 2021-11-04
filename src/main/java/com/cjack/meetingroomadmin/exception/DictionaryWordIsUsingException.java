package com.cjack.meetingroomadmin.exception;

import com.cjack.meetingroomadmin.config.ErrorCodeDefine;


public class DictionaryWordIsUsingException extends Exception{

    private Integer code = ErrorCodeDefine.DICTIONARY_WORD_IS_USING_ERROR;
    private String message = ErrorCodeDefine.DICTIONARY_WORD_IS_USING_MSG;

    public DictionaryWordIsUsingException(){
    }

    public DictionaryWordIsUsingException(String message){
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
