package com.cjack.meetingroomadmin.config;


public class PrivageConfig {

    //角色类型
    public final static String SUPER_ADMIN_CODE = "LEVEL_SUPER";//超级管理员
    public final static String DEALER_ADMIN_CODE = "LEVEL_DEALER";//经销商
    public final static String MASRER_ADMIN_CODE = "LEVEL_MASRER";//校长
    public final static String TEACHER_ADMIN_CODE = "LEVEL_TEACHER";//教师

    //权限相关
    //所有权限
    public final static String ALL = "_ALL_";
    //查看所有学生的权限
    public static String STUDENT_ALL = "STUDENT_ALL";
    //查看学校本校学生的权限
    public static String STUDENT_SCHOOL = "STUDENT_SCHOOL";
    //查看本班学生的权限
    public static String STUDENT_CLASS = "STUDENT_CLASS";



    /**
     * 把字符串数组拼接成逗号分割的字符串
     * @param arr
     * @return
     */
    private static String handleString( String[] arr){
        StringBuilder sb = new StringBuilder();
        for( String str : arr){
            sb.append( str).append( ",");
        }
        return sb.toString();
    }

    /**
     *
     * @param roleContent
     * @param name
     * @return  true 代表有权限，false代表无权限
     */
    public static boolean checkPrivalege( String roleContent, String name){
        if( roleContent.equals( ALL) || roleContent.indexOf( name) != -1){
            return true;
        }
        return false;
    }
}
