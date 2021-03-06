package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 企业助力表
 */
@Data
public class EnterpriseSupportModel extends BaseModel{

    private String name;//类型名称
    private String url;//文件下载地址
    private String size;//文件大小，内容为22MB这种格式
    private Integer downloadCount = 0;//下载次数
    private String cover;//封面图片地址
    private Long typeId;//类型id
    private String typeName;//类型名称
    private String content;//内容
    private String vedioUrl;//视频文件地址
    private Integer sorting;//展示顺序，越高展示越前面
    private Integer viewCount;
    private String phone;//电话

}
