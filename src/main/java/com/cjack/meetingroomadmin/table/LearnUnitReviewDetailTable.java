package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 单元复习记录表
 */
@Entity
@Table(name="learn_unit_review_detail")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LearnUnitReviewDetailTable {

    public LearnUnitReviewDetailTable(){}

    public LearnUnitReviewDetailTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer type;//1-语音学习，2-听音辩意，3-生词本，4-智能听写，5-句子填空，6-句式联系，7-英文选意，8-中文选词

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "learn_unit_id")
    private LearnUnitTable learnUnit;//关联学习的单元id
}
