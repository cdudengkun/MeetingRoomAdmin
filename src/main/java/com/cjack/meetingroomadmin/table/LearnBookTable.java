package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * app用户学习课本记录
 */
@Entity
@Table(name="learn_book")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LearnBookTable {
    public LearnBookTable(){}

    public LearnBookTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Date boughtTime;//购买时间
    private Integer currentBook;//是否当前学习课本，1-是，2-否
    private Integer pass;//是否测试通过，1-是，2-否

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "book_id")
    private BookTable book;//关联课本id

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    private Integer finish;//是否学习完成，1-是，2-否
    private Integer preTest;//是否完成学前测试，1-是，2-否
    private Integer buy;//是否已经购买，1-是，2-否
    /**
     * 这个地方之前是打算每次查询课本信息的时候去 learn_unit_word表统计每个单元的学习的单词数再累加的，这样让数据表字段更精简一点;
     * 但是由于课本、单元的数据是经常去查询的，这样每次都去几张表查询统计的话，性能比较低，这个地方将直接在课本学习表里面添加一个已学习单词数字段
     */
    private Integer learnedNum;//该课本已经学习的单词数

    //学习课本对应的学习单元
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "learnBook" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<LearnUnitTable> learnUnits;


    //课本对应的测试记录
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "learnBook" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<TestBookTable> testBooks;
}
