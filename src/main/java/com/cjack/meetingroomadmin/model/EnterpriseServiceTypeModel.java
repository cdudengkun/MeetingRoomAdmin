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

    private Integer type;//类型，1-企业服务,2-政策解读,3-礼包领取,4-企业助力
    private Integer level;
    private Long parentId;//父节点id，所有二级节点的父节点id都是0

}
