package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 充值卡
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RechargeCardModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Date activeTime;//充值卡激活时间
    private Double amount;//充值卡对应金额
    private String code;//充值卡卡号
    private String description;//充值卡描述
    private Integer validityDay;//有效期（天）
    private Integer status;//充值卡状态，1-未使用，2-已使用

    //充值卡所属学校
    private Long schoolId;
    private String schoolName;
    //充值卡创建人
    private Long adminUserId;
    private String adminUserName;
    //充值卡所属App用户
    private Long appUserId;
    private String appUserName;

    //搜索条件-传值
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startActiveTime;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endActiveTime;

}
