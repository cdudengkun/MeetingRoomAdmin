package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

@Data
public class SchoolBindApplyModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Long schoolId;
    private String schoolName;

    private Long appUserId;
    private String appUserName;
    private String appUserPhone;
    private String appUserUid;

    private Long adminUserId;
    private String adminUserName;
    private Date agreeTime;//同意时间
    private Integer state;//申请状态，1-同意，2-未同意
}
