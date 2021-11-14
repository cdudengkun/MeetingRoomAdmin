package com.cjack.meetingroomadmin.util;

import java.util.Random;

/**
 * Created by root on 5/15/19.
 */
public class CustomerStringUtil {

    /**
     * 拼接字符串，用[joinStrs]替换字符串里面的{n}占位符里面的内容，n从0开始
     * @param str
     * @param joinStrs
     * @return
     */
    public static String handleStrJoin( String str, String [] joinStrs){
        if( str == null || str.equals( "")){
            return str;
        }
        if( joinStrs == null || joinStrs.length == 0){
            return str;
        }
        for( int i =0; i < joinStrs.length ; i++){
            //这里可能会有性能问题
            str = str.replace( "{" + i + "}", joinStrs[i]);
        }
        return str;
    }

    public static String toLikeStr( String str){
        return "%" + str + "%";
    }

    public static String randomNumberStr( int len){
        StringBuffer buffer = new StringBuffer( "0123456789");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for ( int i = 0; i < len; i ++){
            sb.append( buffer.charAt( r.nextInt( range)));
        }
        return sb.toString();
    }

    public static String randomStrOnlyUpperChar( int len){
        StringBuffer buffer = new StringBuffer( "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for ( int i = 0; i < len; i ++){
            sb.append( buffer.charAt( r.nextInt( range)));
        }
        return sb.toString();
    }

    /**
     * 比较两个字符串是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareStr( String str1, String str2){
        if( str1 == null){
            if( str2 == null){
                return true;
            }
            return false;
        }
        return str1.equals( str2);
    }
}
