package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by root on 5/26/19.
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdvertisementModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String imgUrl;//广告图片地址
    private String url;//广告点击跳转地址
    private String sequence;//广告出现顺序，从小到大
    private String title;//广告标题
    private Integer type;//广告类型 1-充值界面广告,2-发现模块广告,3-开屏模块广告
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startTime; //广告开始时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime; //广告结束时间
}
