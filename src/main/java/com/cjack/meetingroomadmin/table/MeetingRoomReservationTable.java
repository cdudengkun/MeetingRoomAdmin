package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 会议室预定信息表
 */
@Entity
@Table(name="meeting_room_reservation", catalog = "meeting_room")
@Data
public class MeetingRoomReservationTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Long startTime;//开始时间
    private Long endTime;//结束时间
    private String name;//联系人姓名
    private String phone;//联系人电话
    private Integer hour;//共计时长


    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_room_id")
    private MeetingRoomTable meetingRoom;// 关联会议室id

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 预定用户id

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "meetingRoom")
    private AppUserOrderTable orderTable;
}
