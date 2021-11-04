package com.cjack.meetingroomadmin.config;

/**
 * Created by root on 5/23/19.
 */
public class ErrorCodeDefine {

    public final static Integer USER_NOT_FOUND = 201;
    public final static String USER_NOT_FOUND_MSG = "该用户不存在";

    public final static Integer USER_PASS_ERROR = 202;
    public final static String USER_PASS_ERROR_MSG = "用户密码错误";

    public final static Integer INTERFACE_TYPE_ERROR = 303;
    public final static String INTERFACE_TYPE_ERROR_MSG = "接口类型错误";

    public final static Integer USER_NOT_LOGIN = 304;
    public final static String USER_NOT_LOGIN_MSG = "用户未登录";

    public final static Integer INTERFACE_COMMON_ERROR = 50000;
    public final static String INTERFACE_COMMON_ERROR_MSG = "系统异常，请稍后再试";

    public final static Integer JPUSH_FAILE_ERROR = 50001;
    public final static String JPUSH_FAILE_ERROR_MSG = "消息推送失败，请联系管理员";

    public final static Integer DICTIONARY_WORD_IS_USING_ERROR = 50002;
    public final static String DICTIONARY_WORD_IS_USING_MSG = "词库单词已经被课本使用，删除失败";

    public final static Integer DICTIONARY_WORD_NOT_FOUND_ERROR = 50003;
    public final static String DICTIONARY_WORD_NOT_FOUND_MSG = "词库单词不存在";

    public final static Integer UNIT_IS_USING_ERROR = 50004;
    public final static String UNIT_IS_USING_MSG = "课本单元已被学习，删除失败";
}
