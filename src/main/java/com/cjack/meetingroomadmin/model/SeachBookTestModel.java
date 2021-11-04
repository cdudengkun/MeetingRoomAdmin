package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 搜索课本测试列表条件
 */
@Data
public class SeachBookTestModel {

    private Long appUserId;
    private Long bookId;//课本id
    private Long schoolClassId;//班级id
    private String name;//学员姓名
    private String phone;//学员电话
}
