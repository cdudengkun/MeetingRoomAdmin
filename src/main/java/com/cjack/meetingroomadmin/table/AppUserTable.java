package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * app用户信息
 */
@Entity
@Table(name="app_user", catalog = "meeting_room")
@Data
public class AppUserTable {

    public AppUserTable(){

    }
    public AppUserTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String avatar;//头像
    private String name;//姓名
    private String loginName;//登录帐号
    private String password;//登录密码
    private String phone;//电话
    private String companyName;//公司名称
    private String wechatOpenId; //绑定微信id
    private Long lastLoginTime;//最后登录时间
    private Integer status;//状态是否可用 1-可用，2-禁用
    @ManyToOne
    @JoinColumn(name="province_id")
    private CityTable province;
    @ManyToOne
    @JoinColumn(name="city_id")
    private CityTable city;
    @ManyToOne
    @JoinColumn(name="country_id")
    private CityTable country;
    private String detail;

    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy="appUser")
    private AppUserAccountTable appUserAccount;//用户账户信息

}
