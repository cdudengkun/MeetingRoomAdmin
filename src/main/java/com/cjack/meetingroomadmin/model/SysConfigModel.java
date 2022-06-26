package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class SysConfigModel {

    private Long id;
    private Long createTime;
    private Long updateTime;

    private String memberRightTitle;
    private String memberRight;

    private String textEnterpriseService;//企业服务
    private String textPolicyInterpretation;//政策解读
    private String textGift;//礼包领取
    private String textEnterpriseSupport;//企业助力

    private String privacyPolicy;//隐私政策
}
