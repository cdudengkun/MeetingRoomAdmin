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
    private String vedioUrl;//视频文件地址

    private String content;//图文详情，只有礼包领取里面有这个
    private String numberNo;//优惠券编号
    private String cover;//封面图片地址
    private Integer sorting;//展示顺序，越高展示越前面
    private Integer viewCount;

    private Integer drawedCount;//优惠券被领取的次数
}
