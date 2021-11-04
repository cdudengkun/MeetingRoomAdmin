package com.cjack.meetingroomadmin.table;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="advertisement")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdvertisementTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String imgUrl;//广告图片地址
    private String url;//广告点击跳转地址
    private String sequence;//广告出现顺序，从小到大
    private String title;//广告标题
    private Integer type;//广告类型
    private String name;//姓名
    private Date endTime;
    private Date startTime;
}
