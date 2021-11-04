package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 学校和校长是一对一关系
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="school")
@Data
public class SchoolTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name="province_id", columnDefinition="int(20)")
    private CityTable province;
    @ManyToOne
    @JoinColumn(name="city_id", columnDefinition="int(20)")
    private CityTable city;
    @ManyToOne
    @JoinColumn(name="country_id", columnDefinition="int(20)")
    private CityTable country;
    private String detail;

    private String name;//学校名称
    private String phone;//学校联系电话
    private String description;//学校介绍
    private String uid;//

    //学校代理商 代理商
    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable agent;

    //学校下的学生
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "school" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<AppUserTable> students;

    //学校下的教师列表，注意校长也属于教师，相当于特殊的教师
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "school" , fetch = FetchType.LAZY)
    private List<AdminUserTable> teachers;

    //学校下的班级
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "school" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<SchoolClassTable> schoolClasses;
}
