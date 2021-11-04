package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 单元-教师点评
 */
@Data
public class LearnUnitCommentModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String content;//点评内容

    //关联单元
    private Long learnUnitId;
    private String learnUnitName;

    //点评教师
    private Long adminUserId;
    private String adminUsername;
}
