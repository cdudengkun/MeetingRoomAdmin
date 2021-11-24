package com.cjack.meetingroomadmin.model;

import lombok.Data;


/**
 * Created by root on 5/26/19.
 */
@Data
public class AdvertisementModel extends BaseModel{

    private String cover;//广告图片地址
    private String url;//广告点击跳转地址
    private String sequence;//广告出现顺序，从小到大
    private String title;//广告标题
    private Integer position;//广告位置 1-首页广告
    private Long adminUserId;//创建人id
}
