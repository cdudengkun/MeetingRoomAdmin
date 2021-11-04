package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 全国城市列表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CityModel {
    private Long id;
    private String areaCode;//地区编码
    private String name;//地区名
    private Integer level;//地区级别（1:省份province,2:市city,3:区县district,4:街道street）
    private String cityCode;//城市编码
    private String center;//城市中心点（即：经纬度坐标）
    private Long parentId;//地区父节点
}
