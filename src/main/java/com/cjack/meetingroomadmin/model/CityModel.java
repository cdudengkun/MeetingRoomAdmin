package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 全国城市列表
 */
@Data
public class CityModel extends BaseModel{

    private Long id;
    private String name;
    private Integer level;
    private Long parentId;//地区父节点
}
