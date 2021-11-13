package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * app用户信息
 */
@Data
public class AppUserModel  extends BaseModel{

    public AppUserModel(){}


    private String avatar;
    private String name;
    private String loginName;//
    private String phone;
    private String companyName;
    private String wechatOpenId;
    private Long lastLoginTime;
    private Long provinceId;
    private String provinceName = "";
    private Long cityId;
    private String cityName = "";
    private Long countryId;
    private String countryName = "";
    private String detail;
    private AppUserAccountModel accountModel;
    private Integer isVip;
    private Integer status;//状态是否可用 1-可用，2-禁用
}
