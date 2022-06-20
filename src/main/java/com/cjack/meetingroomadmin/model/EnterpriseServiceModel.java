package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 企业服务
 */
@Data
public class EnterpriseServiceModel  extends BaseModel{


    private String cover;//封面图片地址
    private String title;//标题
    private String content;//内容
    private Long typeId;//类型id
    private String typeName;//类型名称
    private Integer sorting;//展示顺序，越高展示越前面
    private String vedioUrl;//视频文件地址
    private Integer viewCount;
    private String phone;//电话
}
