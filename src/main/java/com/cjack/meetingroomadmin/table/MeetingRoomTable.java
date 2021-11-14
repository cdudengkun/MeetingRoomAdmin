package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 会议室表
 */
@Entity
@Table(name="meeting_room", catalog = "meeting_room")
@Data
public class MeetingRoomTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String cover;//封面图片地址
    private Integer capicity;//容纳人数
    private Integer price;//价格，元/小时
    private String addrDetail;//会议室楼内具体地址
    private Integer status;//状态，0-未审核，1-审核通过，2-审核拒绝
    private Long adminUserId;//创建人id

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "meeting_zone_id")
    private MeetingZoneTable meetingZone;// 所属会议空间

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "level_id")
    private DefineSignKeyTable level;//规格


}
