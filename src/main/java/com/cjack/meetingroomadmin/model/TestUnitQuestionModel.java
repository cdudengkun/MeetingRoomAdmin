package com.cjack.meetingroomadmin.model;

import com.cjack.meetingroomadmin.table.DictionaryWordTable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 单元测试题目列表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestUnitQuestionModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private TestUnitModel testUnit;//关联单元测试
    private DictionaryWordTable word;//关联单词

    private String title;//题目
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;
    private String phoneticSymbol;//音标
    private String rightItem;//正确值
    private String selectItem;//用户选择值
    private Integer type;//1 英-选汉语 2 汉-选英语 3-读音
}
