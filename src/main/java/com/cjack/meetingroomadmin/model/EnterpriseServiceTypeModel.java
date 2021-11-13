package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 企业服务类型
 */
@Data
public class EnterpriseServiceTypeModel extends BaseModel{

    private String logo;//logo图片地址 未选中时候
    private String logoChosed;//logo图片地址 选中时候
    private String name;//类型名称
    private Integer priority;//展示优先级,越大展示越前面

}
