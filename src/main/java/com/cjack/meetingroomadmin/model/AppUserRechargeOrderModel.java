package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 用户充值订单表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserRechargeOrderModel {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private AppUserModel appUser;// 所属用户
    private Integer amount;//订单金额
    private String memo;//订单说明

}
