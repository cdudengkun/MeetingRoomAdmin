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

    private Integer model;//1-线上优惠券，2-线下优惠券

    private Long meetingZoneId;
    private Long typeId;//类型id
    private String typeName;//类型名称
    private Integer status;//1-未发布，2-已发布

    private String cover;//封面图片地址
}
