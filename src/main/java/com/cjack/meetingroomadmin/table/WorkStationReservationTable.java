package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 工位预定信息表
 */
@Entity
@Table(name="work_station_reservation", catalog = "meeting_room")
@Data
public class WorkStationReservationTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Long reservationTime;//预定日期（工位按天预定），不控制剩余工位数量，他们自己线下控制
    private String name;//联系人姓名
    private String phone;//联系人电话
    private Integer count;//预定工位数量

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_zone_id")
    private MeetingZoneTable meetingZone;// 所属会议空间


    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 预定用户id

}
