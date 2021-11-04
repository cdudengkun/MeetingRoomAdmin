package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 课本测试题目列表
 */
@Entity
@Table(name="test_book_question")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestBookQuestionTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "test_book_id")
    private TestBookTable testBook;//关联课本测试
    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "word_id")
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
