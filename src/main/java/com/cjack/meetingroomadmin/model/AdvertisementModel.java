package com.cjack.meetingroomadmin.model;

import lombok.Data;


/**
 * Created by root on 5/26/19.
 */
@Data
public class AdvertisementModel extends BaseModel{

    private String cover;//广告图片地址
    private Integer urlType;//1-外部网站跳转，2-跳转企业服务，3-跳转政策解读，4-跳转礼包领取，5-跳转企业助力，6-跳转共享办公中心,7-广告图文内容
    private String url;//广告点击跳转地址
    private String sequence;//广告出现顺序，从小到大
    private String title;//广告标题
    private Integer position;//广告位置 1-首页广告
    private Long adminUserId;//创建人id
    private String content;//广告图文内容
    private String phone;//广告方联系电话
    private String btnDesc;//广告方联系电话按钮文字
}
