package com.cjack.meetingroomadmin.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class BatchAddAppUserModel {

    @Id
    private Long schoolId;//学校id
    private Integer number;//生成数量
    private String password;//初始密码

}
