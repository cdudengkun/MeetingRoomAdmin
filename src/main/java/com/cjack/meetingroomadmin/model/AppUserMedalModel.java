package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * app用户获得的奖牌
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserMedalModel {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private AppUserModel appUser;// 所属用户
    private MedalModel medal;// 对应奖牌
}
