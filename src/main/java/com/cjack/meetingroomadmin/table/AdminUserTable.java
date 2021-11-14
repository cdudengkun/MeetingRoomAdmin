package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 后台管理端使用人账户信息
 */
@Entity
@Table(name="admin_user", catalog = "meeting_room")
@Data
public class AdminUserTable {

    public AdminUserTable(){}

    public AdminUserTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String name;//姓名
    private String phone;//电话
    private Long lastLoginTime;//最后登录时间
    private String birthday;//生日
    private String email;//邮箱
    private String avatar;//头像
    private Integer sex;//性别 1-男，2-女
    private String wechatOpenId;//绑定的微信公众号id
    private String loginName;//登录名称
    private String passWord;//登录密码
    private Long adminUserId;//创建人id

    @OneToOne
    @JoinColumn(name="province_id", columnDefinition="bigint(20)")
    private CityTable province;
    @OneToOne
    @JoinColumn(name="city_id", columnDefinition="bigint(20)")
    private CityTable city;
    @OneToOne
    @JoinColumn(name="county_id", columnDefinition="bigint(20)")
    private CityTable county;
    private String detail;

    @ManyToOne(cascade={CascadeType.REFRESH},optional=false)
    @JoinColumn(name="role_id")
    private AdminRoleTable adminRole;

}
