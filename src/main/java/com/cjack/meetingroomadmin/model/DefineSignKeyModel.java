package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 共用的只需要id-key的数据项配置表
 */
@Data
public class DefineSignKeyModel extends BaseModel{

    private Long id;
    private String dataKey;

}

