package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户每日在App上的行为记录；包括 每日签到时间、每日学习单词、每日复习单词 等内容
 */
@Data
public class AppUserDailyLearnInfoModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer daysNumber;//通过mysql的 todays函数转换的createTime相对于公元0点天数，方便后续很多根据天来查询的操作

    private Date punchTime;//每日打卡签到时间  -这个地方后续考虑加个字段来保存日期的天数，方便后续按天查询查询
    private Integer learnWordNum;//每日新学习的单词数
    private Integer reviewWordNum;//每日复习的单词数
    //这个字端应该算是冗余字段，但是没办法，不可能每次去统计学习排名的时候，都去把新学习和复习的单词数做加法运算，这样会影响查询性能，只能加上这个冗余字段
    private Integer allLearnWordNum;//每日总的学习的单词数（它的值为 learnWordNum + reviewWordNum）
    private Integer learnTime;//今日学习时间-单位秒
    private Integer onlineTime;//今日在线时间-单位秒
}
