package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户订单明细表
 */
@Entity
@Table(name="app_user_order_detail", catalog = "meeting_room")
@Data
public class AppUserOrderDetailTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;


    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_order_id")
    private AppUserOrderTable order;

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_room_reservation_id")
    private MeetingRoomReservationTable meetingRoom;//关联会议室预定信息

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "work_station_reservation_id")
    private WorkStationReservationTable workStation;//关联工位预定信息

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_zone_id")
    private MeetingZoneTable meetingZone;//订单所属 会议中心信息

    private Integer amount;//商品单价
    private Integer count;//商品数量
    private Integer type;//商品类型 1-工位，2-会议室,3-vip

}
