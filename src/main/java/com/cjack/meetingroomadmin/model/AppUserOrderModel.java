package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 用户订单表
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class AppUserOrderModel  extends BaseModel{

    private Double amount;
    private String orderNo;
    private String memo;
    private Long payTime;
    private Integer status;
    private Integer vipHour;
    private CouponModel coupon;
    private Integer payType;

}
