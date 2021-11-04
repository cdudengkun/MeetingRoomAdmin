package com.cjack.meetingroomadmin.util;

import org.springframework.util.StringUtils;

import java.util.List;

public class EmptyUtil {

    public static boolean isNotEmpty( Long l){
        return l != null && !l.equals( 0l);
    }

    public static boolean isNotEmpty( Integer i){
        return i != null && !i.equals( 0);
    }

    public static boolean isNotEmpty( String s){
        return !StringUtils.isEmpty( s);
    }

    public static boolean isNotEmpty(List<?> datas){
        if( datas != null && datas.size() > 0){
            return true;
        }else {
            return false;
        }
    }
}
