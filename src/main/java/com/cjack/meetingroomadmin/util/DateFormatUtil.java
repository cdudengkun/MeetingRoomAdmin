package com.cjack.meetingroomadmin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    public final static String DATE_RULE_1 = "yyyy年MM月dd日 HH:mm:ss";
    public final static String DATE_RULE_2 = "yyyy-MM-dd";
    public final static String DATE_RULE_3 = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_RULE_4 = "yyMM";
    public final static String DATE_RULE_5 = "yyyy-MM";
    public final static String DATE_RULE_6 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final static String DATE_RULE_7 = "dd-MM-yyyy";
    public final static String DATE_RULE_8 = "MM-dd";

    public static String format( Date date, String rule){
        DateFormat df = new SimpleDateFormat( rule);
        return df.format( date);
    }

    /**
     * 返回当前时间前30天的日期
     * @return  yyyy-MM-dd
     */
    public static Date formatLastNDay( Integer n){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( System.currentTimeMillis());
        c.add( Calendar.DATE, -n);
        return c.getTime();
    }

    public static String format( String dateStr, String rule) {
        DateFormat df = new SimpleDateFormat( rule);
        try {
            return df.format( new Date( dateStr));
        } catch (Exception e) {
            return dateStr;
        }
    }

    public static Date parse( String dateStr) {
        DateFormat df = new SimpleDateFormat( DATE_RULE_2);
        try {
            return df.parse( dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays( Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2){   //不同一年
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++){
                if(i%4==0 && i%100!=0 || i%400==0){    //闰年
                    timeDistance += 366;
                }
                else{    //不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2-day1) ;
        }
        else{    //同一年
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

    /**
     * 获取当前时间的月份
     * @return
     */
    public static Date getThisMonthFirstDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( System.currentTimeMillis());
        cal.set( Calendar.DAY_OF_MONTH, 1);
        cal.set( Calendar.HOUR_OF_DAY, 0);
        cal.set( Calendar.MINUTE, 0);
        cal.set( Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static void main(String[] args) {
        System.out.println( getThisMonthFirstDay());
    }
}
