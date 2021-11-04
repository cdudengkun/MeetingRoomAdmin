package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 学校班级信息
 */
@Entity
@Table(name="school_class")
@Data
public class SchoolClassTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable adminUser;//班级创建人

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "school_id")
    private SchoolTable school;//所属学校

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "teacher_id")
    private AdminUserTable teacher;//班主任

    private String name;//班级名称
    private String description;//班级描述

    //班级下的学生
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "schoolClass" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<AppUserTable> students;
}
