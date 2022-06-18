package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class SysConfigModel {

    private Long id;
    private Long createTime;
    private Long updateTime;

    private String enterpriseSupportAdrContent;//资源共享广告跳转页面图文内容
    private String enterpriseSupportAdrPhone;//资源共享广告跳转页面联系电话
    private String enterpriseSupportAdrBtnDescription;//资源共享广告跳转页面按钮描述

    private String couponAdrContent;//礼包领取页面广告的图文内容
    private String couponAdrBtnDescription;//礼包领取页面广告的按钮描述

    private Integer menmberPrice1;
    private Integer menmberPrice3;
    private Integer menmberPrice12;
    private String memberRight;
    private String memberWelfare;//会员福利，多个英文逗号分割

    private String textEnterpriseService;
    private String textPolicyInterpretation;
    private String textGift;
    private String textEnterpriseSupport;

}
