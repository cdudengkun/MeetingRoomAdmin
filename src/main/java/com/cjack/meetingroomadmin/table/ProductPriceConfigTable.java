package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="product_price_config", catalog = "meeting_room")
@Data
public class ProductPriceConfigTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;


    private String cover;//服务内容介绍图片
    private String name;//价格名称
    private Integer serviceCount;//价格对应服务的月份，1一个月就是1，一年就是12
    private Integer price;//原价
    private String simpleIntroduction;//这个产品提供的服务介绍 简略
    private String introduction;//这个产品提供的服务介绍


}
