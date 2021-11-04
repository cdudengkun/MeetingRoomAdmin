package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * Created by root on 5/26/19.
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SchoolModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String provinceName;
    private Long provinceId;
    private String cityName;
    private Long cityId;
    private String countryName;
    private Long countryId;
    private String detail;

    private Integer studentNumber;//学员数量
    private Integer classNumber;//班级数量
    private Integer learnsOfSevenDay;//最近七天学习人数
    private Integer learnsOfThrityDay;//最近30天学习人数
    private String name;//学校名称
    private String phone;//学校联系电话
    private String description;//学校介绍
    private String uid;//
    //学校代理商 代理商
    private Long agentId;
    private String agentName;

    //校长
    private Long masterId;
    private String masterName;



}
