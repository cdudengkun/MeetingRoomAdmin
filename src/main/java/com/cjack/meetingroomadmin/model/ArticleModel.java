package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 阅读精选
 * @author sww
 * @create 2020-01-13 17:18
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ArticleModel {

    Long id ;
    private Date createTime;
    private Date updateTime;

    //作者
    private String author;

    private String img;

    //标题
    private String title;

    //简介
    private String introduction;

    //类型,1:小学,2:初中,3:高中
    private Integer type;

}
