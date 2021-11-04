package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 学校班级信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SchoolClassModel {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private String name;//班级名称
    private String description;//班级描述
    private Integer studentNumber;//学员数量
    private Integer learnsOfTwoDay;//最近两天学习人数
    private Integer learnsOfSevenDay;//最近七天学习人数
    private String teacherName;//班主任

    //传值
    private Long adminUserId;//添加人
    private Long teacherId;//班主任id
    private Long schoolId;//所属学校
    private String schoolName;
}
