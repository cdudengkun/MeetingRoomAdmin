package com.cjack.meetingroomadmin.model;

import com.cjack.meetingroomadmin.table.DictionaryWordTable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * App用户学习的单词记录
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LearnUnitWordModel {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private LearnUnitModel learnUnit;//关联学习的单元id
    private DictionaryWordTable word;//关联单词
}
