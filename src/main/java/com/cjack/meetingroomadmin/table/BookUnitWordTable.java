package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 课本单元单词信息
 */
@Entity
@Table(name="book_unit_word")
@Data
public class BookUnitWordTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "book_id")
    private BookTable book;//所属课本
    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "unit_id")
    private BookUnitTable unit;//所属课本单元

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "word_id")
    private DictionaryWordTable dicWord;//对应单词
}
