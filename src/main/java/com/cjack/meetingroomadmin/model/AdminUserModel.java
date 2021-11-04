package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 后台管理端使用人账户信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdminUserModel {

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

    private String provinceName;
    private Long provinceId;
    private String cityName;
    private Long cityId;
    private String countryName;
    private Long countryId;
    private String detail;
    private String roleName;
    private Long roleId;
    private String roleContent;
    //校长绑定学校
    private SchoolModel schoolModel;
    //校长、老师所属学校
    private Long schoolId;
    private String schoolName;
}