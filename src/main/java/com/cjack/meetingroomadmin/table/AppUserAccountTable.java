package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * app用户账户信息
 */
@Entity
@Table(name="app_user_account", catalog = "meeting_room")
@Data
public class AppUserAccountTable {

    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Integer isVip;//是否vip 1-是，2-否  这个返回给app的时候，需要计算过期时间，如果已过期，把它修改为2
    private Long joinVipTime;//加入vip时间
    private Long vipExpireTime;//vip过期时间

    private Integer surplusMeetingRoomHour;//剩余会议室时长，单位小时
    private Integer usedMeetingRoomHour;//已用会议室时长，单位小时
    private Integer surplusWorkStation;//剩余工位数，单位个/天
    private Integer usedWorkStation;//已用工位数，单位个/天

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户
}
