package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 名师
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FamousTeacherModel {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private String img;//名师图片
    private Double score;//评分
    private String title;//标题
    private String name;//姓名
    private String description;//名师介绍
    private Integer visitors;//观看人数
    private String url;//
}
