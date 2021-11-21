package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * App用户反馈意见表
 */
@Data
public class FeedBackModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String content;//反馈内容
    private String name;//联系人姓名
    private String phone;//联系人电话
}
