package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 收获地址信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ReceiverAddressModel {


    private Long id;
    private Date createTime;
    private Date updateTime;

    private String phone;//收件人电话
    private String receiverName;//收件人姓名
    private AppUserModel appUser;// 所属用户

    private CityModel province;
    private CityModel city;
    private CityModel country;
    private String address;
}
