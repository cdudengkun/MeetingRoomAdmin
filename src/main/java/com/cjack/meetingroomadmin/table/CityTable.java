package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 全国城市列表
 */
@Entity
@Table(name="city", catalog = "meeting_room")
@Data
public class CityTable {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private String areaCode;//地区编码
    private String name;//地区名
    private Integer level;//地区级别（1:省份province,2:市city,3:区县district,4:街道street）
    private String cityCode;//城市编码
    private String center;//城市中心点（即：经纬度坐标）
    private Long parentId;//地区父节点
}
