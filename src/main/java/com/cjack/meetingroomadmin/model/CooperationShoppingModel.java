package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 合作商城
 */
@Data
public class CooperationShoppingModel  extends BaseModel{

    private String shop;
    private String companyName;
    private String name;
    private String phone;
    private String content;
    private String cover;
    private String typeName;
    private Long typeId;
}
