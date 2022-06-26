package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="product_price_type", catalog = "meeting_room")
@Data
public class ProductPriceTypeTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String name;//服务名称
    private Integer sort;


}
