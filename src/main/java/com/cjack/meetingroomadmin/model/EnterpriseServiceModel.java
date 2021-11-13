package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 企业服务
 */
@Data
public class EnterpriseServiceModel  extends BaseModel{


    private String cover;//封面图片地址
    private String title;//消息标题
    private String content;//消息内容
}
