package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 优惠券
 */
@Entity
@Table(name="coupon", catalog = "meeting_room")
@Data
public class CouponTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Integer mount;//金额
    private String name;//优惠券名称
    private Long startTime;//可用开始时间
    private Long endTime;//过期时间

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_zone_id")
    private MeetingZoneTable meetingZone;// 所属会议空间
}