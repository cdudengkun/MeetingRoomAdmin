package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 优惠券
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class CouponModel  extends BaseModel{

    private Integer mount;
    private String name;
    private Long startTime;
    private Long endTime;
    private Integer drawed;

    private Long meetingZoneId;
}
