package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 用户订单表
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class AppUserOrderModel  extends BaseModel{

    private Integer amount;
    private Integer payAmount;//实际金额
    private String orderNo;
    private String memo;
    private Long payTime;
    private Integer status;
    private Integer vipHour;
    private CouponModel couponModel;
    private Integer payType;
    private String recommender;//推荐人
    private String name;//姓名
    private String phone;//电话
    private Long vipExpireTime;//vip过期时间

    private Long payTimeStart;//支付时间 查询条件开始
    private Long payTimeEnd;//支付时间 查询条件结束

    private AppUserModel appUserModel;

    private MeetingRoomReservationModel meetingRoomReservationModel;
    private WorkStationReservationModel workStationReservationModel;
    private MeetingZoneModel meetingZoneModel;

    private Integer type;//商品类型 1-工位，2-会议室,3-vip
}
