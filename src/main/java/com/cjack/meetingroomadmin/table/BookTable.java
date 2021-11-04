package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 课本信息表
 */
@Entity
@Table(name="book")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class BookTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String bookName;//课本名称
    private Integer downLoadNum;//课本下载记录
    private Integer wordsNum;//课本单词数

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable adminUser;// 课本创建人

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "version_id")
    private BookVersionTable version;// 课本版本

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "grade_id")
    private DefineSignKeyTable grade;// 课本年级

    private String coverImg;//封面图片地址
    private Double price;//课本价格

    //学习课本下面的所有单元
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "book" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<BookUnitTable> bookUnits;

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "book" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<LearnBookTable> learnBooks;
}
