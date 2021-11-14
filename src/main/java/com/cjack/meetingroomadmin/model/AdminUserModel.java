package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 后台管理端使用人账户信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdminUserModel  extends BaseModel{

    private String name;//姓名
    private String phone;//电话
    private Long lastLoginTime;//最后登录时间
    private String email;//邮箱
    private String avatar;//头像
    private Integer sex;//性别 1-男，2-女
    private String loginName;//登录名称
    private String passWord;//登录密码

    private String provinceName;
    private String provinceId;
    private String cityName;
    private String cityId;
    private String countyName;
    private String countyId;
    private String detail;
    private String roleName;
    private Long roleId;
    private String roleContent;
}
