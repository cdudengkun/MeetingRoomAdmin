package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 复习测试记录
 */
@Entity
@Table(name="test_review")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestReviewTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer useTime;//测试用时，单位秒
    private Integer score;//测试得分

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;//测试人App用户id
    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "learn_unit_review_detail_id")
    private LearnUnitReviewDetailTable LearnUnitReviewDetail;//关联用户学习单元复习记录id

    //本次测试对应的复习记录
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "testReview" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<TestReviewQuestionTable> questions;
}
