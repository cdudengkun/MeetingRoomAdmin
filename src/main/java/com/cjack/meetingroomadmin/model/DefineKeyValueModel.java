package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 共用的key-value数据项配置表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DefineKeyValueModel {

    private Long id;
    private String dataKey;//数据项名称
    private String dataValue;//数据项值

    private Integer type;//1-积分等级规则

    private Date createTime;
    private Date updateTime;
}

