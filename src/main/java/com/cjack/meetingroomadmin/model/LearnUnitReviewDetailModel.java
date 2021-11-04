package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 单元复习记录表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LearnUnitReviewDetailModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer type;//1-语音学习，2-听音辩意，3-生词本，4-智能听写，5-句子填空，6-句式联系，7-英文选意，8-中文选词
    private LearnUnitModel learnUnit;//关联学习的单元id
}
