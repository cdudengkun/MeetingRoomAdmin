package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * App用户反馈意见表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FeedBackModel {

    private Long id;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    private Date createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    private Date updateTime;
    private String replyContent;//回复内容
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    private Date replyTime;//回复时间
    private String content;//反馈内容
    private String appUserName;// 反馈人，关联app用户表
    private String adminUserName;// 回复人，关联admin_user
    private Long adminUserId;
    private Integer state;//1-已解决，2-未解决
    private Integer type;//1-用户反馈，2-错误单词反馈
    private String img;//问题反馈截图
}
