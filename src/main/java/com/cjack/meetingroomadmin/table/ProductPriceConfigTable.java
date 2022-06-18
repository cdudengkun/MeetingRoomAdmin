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

    private Integer type;//1-会员

    private Integer price;//原价
    private String name;//产品服务名称
    private Integer disCountPrice;//优惠价格
    private String disCountIntroduction;//折扣活动说明
    private String simpleIntroduction;//这个产品提供的服务介绍 简略
    private String introduction;//这个产品提供的服务介绍

    private String followPriceIntroduction;//继续续费的说明


}
