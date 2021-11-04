package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * app-帮助文档
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HelpDocModel {
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String title;//标题
    private String subContent;//次级内容
    private String content;//内容
}
