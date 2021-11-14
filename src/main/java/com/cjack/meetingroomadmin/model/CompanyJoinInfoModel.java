package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 公司加入申请表
 */
@Data
public class CompanyJoinInfoModel  extends BaseModel{

    private Long provinceId;
    private String provinceName = "";
    private Long cityId;
    private String cityName = "";
    private Long countyId;
    private String countyName = "";
    private String detail;

    private String companyName;//公司名称
    private String name;//联系人
    private String phone;//联系电话
    private String licence;//营业执照列表，多个英文逗号分割
}
