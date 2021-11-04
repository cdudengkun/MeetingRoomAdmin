package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * app用户信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    @DateTimeFormat(pattern="yyyy-MM-dd")
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

    private Integer bookNumber;//开通课本数量
    private String schoolClassName;//班级
    private Long schoolClassId;
    private String schoolName;//学校
    private Long schoolId;

    private String provinceName;
    private Long provinceId;
    private String cityName;
    private Long cityId;
    private String countryName;
    private Long countryId;
    private String detail;

    private String memoName;//学员备注昵称
    private String memoPhone;//学员备注电话
}
