package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 批量生成充值卡
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BatchAddRechargeCardModel {

    private Double amount;//每张充值卡对应金额
    private Integer validityDay;//有效期(天)
    private Integer number;//充值卡的数量
    private String description;//充值卡描述
    private Long adminUserId;
}
