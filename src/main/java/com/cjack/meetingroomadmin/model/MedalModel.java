package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 奖牌配置表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MedalModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String name;//奖牌名称
    private String abbrName;//奖牌缩写名称(大写拼音首字母)
    private Integer level;//奖牌级别（1-金，2-银，-3铜）
}
