package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 课本测试记录
 */
@Data
public class TestBookModel {

    //学习课本id
    private Long learnBookId;
    //aoo用户id
    private Long appUserId;
    //学员姓名
    private String appUserName;
    //课本名称
    private String bookName;
    //学前成绩
    private Integer beforeLearnTestScore;
    //学后成绩
    private Integer afterLearnTestScore;
    //最后测试时间
    private Date lastTestTime;
}
