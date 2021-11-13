package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * App用户反馈意见表
 */
@Entity
@Table(name="feed_back", catalog = "meeting_room")
@Data
public class FeedBackTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String content;//反馈内容
    private String name;//联系人电话
    private String phone;//联系人姓名

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 反馈人，关联app用户表
}
