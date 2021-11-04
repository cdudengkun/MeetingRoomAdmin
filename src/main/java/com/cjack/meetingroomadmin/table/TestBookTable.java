package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 课本测试记录
 */
@Entity
@Table(name="test_book")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestBookTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer preLearnTest;//是否学前测试，1-是，2-否
    private Integer useTime;//测试用时，单位秒
    private Integer score;//测试得分

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "learn_book_id")
    private LearnBookTable learnBook;//关联的用户学习课本

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;//测试人App用户id

    //本次测试对应的题目
    @OneToMany( cascade = {CascadeType.ALL},mappedBy = "testBook" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<TestBookQuestionTable> questions;
}
