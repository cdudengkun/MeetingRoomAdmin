package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * app用户信息
 */
@Entity
@Table(name="app_user")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserTable {

    public AppUserTable(){

    }
    public AppUserTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Date birthDay;//生日
    private String avatar;//头像
    private String name;//姓名
    private String loginName;//登录帐号
    private String password;//登录密码
    private String phone;//电话
    private Integer sex;//性别，1-男，2-女
    private String description;//个人签名说明
    private String qqOpenId;
    private String wechatOpenId;
    private String weiboOpenId;
    private String uid;
    private Integer registType;//1-批量创建，2-App注册
    private Date lastLoginTime;//最后登录时间
    private Integer tryOuted;//是否已经试用过课本，1-是，2-否

    private String memoName;//学员备注昵称
    private String memoPhone;//学员备注电话

    @ManyToOne
    @JoinColumn(name="province_id", columnDefinition="bigint(20)")
    private CityTable province;
    @ManyToOne
    @JoinColumn(name="city_id", columnDefinition="bigint(20)")
    private CityTable city;
    @ManyToOne
    @JoinColumn(name="country_id", columnDefinition="bigint(20)")
    private CityTable country;
    private String detail;
    @ManyToOne
    @JoinColumn(name="school_id", columnDefinition="bigint(20)")
    private SchoolTable school;
    @ManyToOne
    @JoinColumn(name="class_id", columnDefinition="bigint(20)")
    private SchoolClassTable schoolClass;

    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy="appUser")
    private AppUserAccountTable appUserAccount;//用户账户信息

    //用户学习的所有课本
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "appUser" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<LearnBookTable> learnBooks;

    //用户名下的所有奖牌
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "appUser" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<AppUserMedalTable> medals;

    //用户收藏的单词
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "appUser" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<FavoritesWordTable> favoritesWords;

    //用户名下的收货地址列表
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "appUser" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<ReceiverAddressTable> receiverAddresss;
}
