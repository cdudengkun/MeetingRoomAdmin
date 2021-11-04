package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * app用户账户信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserAccountModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Double goldCoin;//用户充值的金币余额
    private Integer integral;//用户积分余额

    private AppUserModel appUser;// 所属用户

    private Integer level;//用户的学习级别，从1-15，分别代表1-15级
}
