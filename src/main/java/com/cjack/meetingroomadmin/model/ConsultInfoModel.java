package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 咨询信息表
 */
@Data
public class ConsultInfoModel extends BaseModel{

    private String title;//问题标题
    private String content;//解答内容
    private String keyWords;//关键字，多个关键字英文逗号分割
    private Integer isHot;//是否热门，1-是，2-否
    private Integer matchCount;//匹配次数

}
