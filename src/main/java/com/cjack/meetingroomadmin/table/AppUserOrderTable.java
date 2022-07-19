package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户订单表
 */
@Entity
@Table(name="app_user_order", catalog = "meeting_room")
@Data
public class AppUserOrderTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;


    private Integer amount;//订单金额
    private Integer payAmount;//实际金额
    private String orderNo;//订单编号
    private String memo;//订单说明
    private Integer payType;//支付方式，这里默认全部都是 1-微信支付
    private Long payTime;//支付时间
    private Integer status;//状态，1-待付款，2-进行中，3-已完成，4-用户申请取消，5-取消审核通过，6-取消审核未通过
    private Integer vipHour;//如果是会议室，则是订单使用会员权益时长；如果是工位，则是会员权益天数
    private Integer type;//商品类型 1-工位，2-会议室,3-vip

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_coupon_id")
    private AppUserCouponTable appUserCoupon;//使用优惠券

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_room_reservation_id")
    private MeetingRoomReservationTable meetingRoom;//关联会议室预定信息

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "work_station_reservation_id")
    private WorkStationReservationTable workStation;//关联工位预定信息

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_zone_id")
    private MeetingZoneTable meetingZone;//订单所属 会议中心信息

}
