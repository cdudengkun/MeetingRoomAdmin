package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 名人名言
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FamousQuotesModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String title;//标题
    private String content;//内容
    private String translation;//中文说明
}
