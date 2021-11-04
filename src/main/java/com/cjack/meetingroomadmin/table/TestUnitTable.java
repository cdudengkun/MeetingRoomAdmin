package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 单元测试记录
 */
@Entity
@Table(name="test_unit")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestUnitTable {

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
    @JoinColumn( name = "learn_unit_id")
    private LearnUnitTable learnUnit;//关联用户学习单元
    //本次测试对应的问题详细
    @OneToMany( cascade = {CascadeType.ALL},mappedBy = "testUnit" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<TestUnitQuestionTable> questions;
}
