package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class RechargeCardCount {
    private Integer total;//已添加总数
    private Integer noActiveTotal;//未激活总数
    private Integer activeTotal;//激活总数
    private Integer expireTotal;//已过期总数
}
