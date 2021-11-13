package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * id must is 1
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="app_config", catalog = "meeting_room")
@Data
public class AppConfigTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;

    /**
     * 免费体验时间
     */
    int freeExperienceTime = 3;

    /**
     * 打卡学习时间
     */
    int cardLearningTime = 20;//min
    /**
     * 打卡几天可以获得积分
     */
    int getScoreCardTime = 5; //5;
    /**
     * 打卡满天数以后获得的积分
     */
    int cardScroeForFullDay = 50 ; //50
    /**
     *  打卡每天获得的积分
     */
    int avrageCardScrore = 10 ; //10
    /**
     * 充值奖励积分
     */
    int rechargeScore = 30;


    int ad_width_ratio =  6 ;
    int ad_height_ratio = 2;

    /**
     *一个单词获得的积分
     */
    int everyWordScore  = 1;
    int everyWordScoreUp = 100 ;//获得积分上限

    /**
     //复习单词获得的积分 todo；
     */
    int everyOldWordScore = 1 ;
    float everyOldWordScoreUp = 100 ;//获得积分上限

    /**
     * 学习10分钟，可得积分数有效学习 (有效学习获得的积分)
     */
    int studyScore = 1;
    int studyScoreUp = 60 ;//获得积分上限

    //闯关通过；
    int passTestScore = 10;
    int passTestScoreUp = 20;//获得积分上限

    /**
     * 分享奖励积分
     */
    int shareScore = 10;
    int shareScoreUp = 30;//获得积分上限
}
