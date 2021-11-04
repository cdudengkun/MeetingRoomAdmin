package com.cjack.meetingroomadmin.config;


public class CommonConfig {

    //SESSION里面的保存的登录信息
    public static String SESSION_NAME = "adminUserId";

    /**
     * 极光推送的时候额外的字段key ：
     * data:{json数据}
     */
    public static final String JPUSH_JSON_EXTRAS_KEY ="data" ;

    //系统数据表里面所有有关是或者否的配置
    public static int YES = 1;
    public static int NO = 2;
}
