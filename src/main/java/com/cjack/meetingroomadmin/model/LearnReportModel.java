package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户学习报告信息
 */
@Data
public class LearnReportModel {

    private Long appUserId;
    //班级id
    private Long schoolClassId;
    //学员电话
    private String phone;
    //学员姓名
    private String name;
    //学员班级
    private String className;
    //最近登录时间
    private Date lastLoginTime;
    //累积在线时长
    private Integer totalOnlineTime;
    //有效学习时长
    private Integer totalLearnTime;
    //单词记忆总量(新学习的单词)
    private Integer totalLearnWords;
    //今日新学单词
    private Integer todayNewWords;
    //今日复习单词
    private Integer todayReviewWords;
    //今日在线时长
    private Integer todayOnlineTime;
    //今日有效时长
    private Integer todayLearnTime;
    //学习进度
    private String learnRate;

}
