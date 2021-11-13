package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class BaseModel {

    private Long id;
    private Long createTime;
    private Long updateTime = System.currentTimeMillis();
    private Long adminUserId;
}
