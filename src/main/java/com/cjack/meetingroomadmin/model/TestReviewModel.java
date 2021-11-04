package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 复习测试记录
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestReviewModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer useTime;//测试用时，单位秒
    private Integer score;//测试得分

    private AppUserModel appUser;//测试人App用户id
    private LearnUnitReviewDetailModel LearnUnitReviewDetail;//关联用户学习单元复习记录id

    //本次测试对应的复习记录
    private List<TestReviewQuestionModel> questions;
}
