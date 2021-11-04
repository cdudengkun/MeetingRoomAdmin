package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 单元学习记录表
 */
@Entity
@Table(name="learn_unit")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LearnUnitTable {

    public LearnUnitTable(){}

    public LearnUnitTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer currentLearnUnit;//1-是，2-否

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "unit_id")
    private BookUnitTable unit;//关联单元
    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "learn_book_id")
    private LearnBookTable learnBook;//对应的学习课本

    private Integer finish;//是否学习完成，1-是，2-否
    private Integer reviewState;//单元的复习状态,值为0代表学习完成，复习测试时间为效果检测后的第1、2、4、7、15天，共五次复习
    private Date finishedDate;//学习完成时间
    private Integer stage;//单元学习阶段 1-认知记忆，2-单词强化，3-效果检测
    /**
     * 这个地方之前是打算每次查询课本信息的时候去 learn_unit_word表统计每个单元的学习的单词数再累加的，这样让数据表字段更精简一点;
     * 但是由于课本、单元的数据是经常去查询的，这样每次都去几张表查询统计的话，性能比较低，这个地方将直接在课本学习表里面添加一个已学习单词数字段
     */
    private Integer learnedNum;//该单元已经学习的单词数

    //单元对应的复习记录
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "learnUnit" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<LearnUnitReviewDetailTable> learnUnitReviewDetails;
    //单元对应的测试记录
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "learnUnit" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<TestUnitTable> testUnits;
    //本单元对应的教师点评
    @OneToMany( cascade = {CascadeType.ALL},mappedBy = "learnUnit" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<LearnUnitCommentTable> comments;
}
