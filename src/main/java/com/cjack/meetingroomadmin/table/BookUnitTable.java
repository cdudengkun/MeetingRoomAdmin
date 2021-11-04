package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 课本单元信息
 */
@Entity
@Table(name="book_unit")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookUnitTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String name;//单元名称
    private Integer wordNum;//该单元的单词数

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "book_id")
    private BookTable book;//所属课本
    //单元对应的测试记录
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "unit" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<BookUnitWordTable> words;
}
