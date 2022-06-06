package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="sys_config", catalog = "meeting_room")
@Data
public class SysConfigTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String enterpriseSupportAdrContent;//资源共享广告跳转页面图文内容
    private String enterpriseSupportAdrPhone;//资源共享广告跳转页面联系电话
    private String enterpriseSupportAdrBtnDescription;//资源共享广告跳转页面按钮描述

    private String couponAdrContent;//礼包领取页面广告的图文内容
    private String couponAdrBtnDescription;//礼包领取页面广告的按钮描述

}
