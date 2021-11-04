package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 单元测试记录
 */
@Data
public class TestUnitModel {

    Long learnUnitId;
    String unitName;//单元名称
    //这里查询最后一次测试的信息
    Date testTime;//测试时间
    Integer useTime;//测试用时，单位秒
    Integer score;//测试得分

    //统计该单元的复习信息
    Integer reviewNumber1;//单元的语音复习次数
    Integer reviewNumber4;//单元的智能听写次数
    Integer reviewNumber5;//单元的句子填空次数
    Integer reviewNumber6;//单元的句式练习次数
}
