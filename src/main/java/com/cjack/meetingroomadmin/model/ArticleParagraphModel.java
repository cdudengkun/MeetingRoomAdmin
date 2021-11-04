package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 阅读精选-段落信息
 * @author sww
 * @create 2020-01-13 17:18
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ArticleParagraphModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Long articleId;

    private String original;//原文
    private String translation;//翻译

}
