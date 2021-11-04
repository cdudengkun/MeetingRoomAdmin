package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class LastSellBookInfoModel {

    private Integer sell_total = 0;//激活总数
    private Integer sell_thirty = 0;//30天内激活数
}
