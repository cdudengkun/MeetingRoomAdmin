package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 用户订单表
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class OrderStatementQueryModel extends BaseModel{

    private Integer type;//统计类型，1-日，2-周，3-月
    private Long meetingZoneId;//订单成交地点
    private String company;//公司名称
}
