package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * app用户账户信息
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class AppUserAccountModel  extends BaseModel{


    private Long id;
    private Integer isVip;
    private Long joinVipTime;
    private Long vipExpireTime;
    private Integer surplusMeetingRoomHour;
    private Integer usedMeetingRoomHour;
    private Integer surplusWorkStation;
    private Integer usedWorkStation;
}
