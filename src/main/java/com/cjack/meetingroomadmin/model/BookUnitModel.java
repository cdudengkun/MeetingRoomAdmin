package com.cjack.meetingroomadmin.model;

import com.cjack.meetingroomadmin.table.DictionaryWordTable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 课本单元信息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookUnitModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String name;//单元名称
    private Integer wordNum;//该单元的单词数

    private BookModel book;//所属课本
    //单元对应的测试记录
    private List<DictionaryWordTable> words;
}
