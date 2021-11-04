package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * App用户反馈意见表
 */
@Entity
@Table(name="feed_back")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FeedBackTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String replyContent;//回复内容
    private Date replyTime;//回复时间
    private String content;//反馈内容

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 反馈人，关联app用户表
    @ManyToOne( cascade={CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "word_id")
    private DictionaryWordTable word;//type为2的时候，关联单词

    @OneToOne( cascade = {CascadeType.ALL})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable adminUser;// 回复人，关联admin_user
    private Integer state;//1-已解决，2-未解决
    private Integer type;//1-用户反馈，2-错误单词反馈
    private String img;//问题反馈截图
}
