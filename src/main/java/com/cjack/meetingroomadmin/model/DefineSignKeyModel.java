package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 共用的只需要id-key的数据项配置表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DefineSignKeyModel {

    private Long id;
    private String dataKey;//数据项名称

    private Integer type;//1-课本版本（人教版、鲁教版）,2-课本阶段（小学、初中、高中）,3-课本年纪

    private Date createTime;
    private Date updateTime;
}

