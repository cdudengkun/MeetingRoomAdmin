package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 共用的key-value数据项配置表
 */
@Data
public class DefineKeyValueModel {
    private Long id;
    private String dataKey;//数据项名称
    private String dataValue;//数据项值

    private Integer type;//1-会议中心设施服务,2-加入我们图片说明

    private Long createTime;
    private Long updateTime;
}

