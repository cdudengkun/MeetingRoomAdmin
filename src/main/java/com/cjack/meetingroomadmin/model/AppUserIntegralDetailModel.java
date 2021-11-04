package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 用户的积分操作记录表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserIntegralDetailModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer number;//本次积分变更金额
    private Integer type;//0-打卡获得积分，1-分享获得积分,2-充值奖励积分,3-购物抵消积分,4-学习时间奖励积分，5-学习单词奖励积分，6-闯关成功获得积分,7-消费积分
    private String memo;//操作说明
    private AppUserModel appUser;// 所属用户

}
