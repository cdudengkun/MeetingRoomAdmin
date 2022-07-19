package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 会议中心表
 */
@Entity
@Table(name="meeting_zone", catalog = "meeting_room")
@Data
public class MeetingZoneTable{


    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    @ManyToOne
    @JoinColumn(name="province_id")
    private CityTable province;
    @ManyToOne
    @JoinColumn(name="city_id")
    private CityTable city;
    @ManyToOne
    @JoinColumn(name="county_id")
    private CityTable county;
    private String detail;

    private String name;//名称
    private String description;//介绍
    private String facilityIds;//设施服务id列表，多个英文逗号分割
    private String cover;//封面图片地址
    private String lnt;//经度
    private String lat;//维度
    private String availableDayTime;//每日可用时间 保存8:00AM-18:00PM这种格式
    private Integer availableWeekDay;//每周可用时间 1-周一到周五，2-周一到周日，3-仅周末
    private String imgs; //图片地址列表，多个英文逗号分割
    private Long adminUserId;//创建人id

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE},mappedBy = "meetingZone")
    @OrderBy("id asc")
    private List<MeetingRoomTable> meetingRooms;

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE},mappedBy = "meetingZone")
    @OrderBy("id asc")
    private List<AppUserOrderTable> orders;

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE},mappedBy = "meetingZone")
    @OrderBy("id asc")
    private List<WorkStationReservationTable> workStationReservations;
}
