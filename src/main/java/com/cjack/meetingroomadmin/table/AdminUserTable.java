package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 后台管理端使用人账户信息
 */
@Entity
@Table(name="admin_user")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdminUserTable {

    public AdminUserTable(){}

    public AdminUserTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String name;//姓名
    private String phone;//电话
    private Date lastLoginTime;//最后登录时间
    private String birthday;//生日
    private String email;//邮箱
    private String avatar;//头像
    private Integer sex;//性别 1-男，2-女
    private String wechatOpenId;//绑定的微信公众号id
    private String loginName;//登录名称
    private String passWord;//登录密码

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

    @ManyToOne(cascade={CascadeType.REFRESH},optional=false)
    @JoinColumn(name="role_id")
    private AdminRoleTable adminRole;

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "unit" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<DictionaryWordTable> words;

    //经销商账户下面的学校列表
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "agent" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<SchoolTable> agentSchools;

    //教师账户下面的班级列表
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "teacher" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<SchoolClassTable> schoolClasses;

    //校长、老师所属的学校
    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "school_id")
    private SchoolTable school;
}
