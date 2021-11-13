package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * app用户阅读系统消息的情况
 */
@Entity
@Table(name="message_read", catalog = "meeting_room")
@Data
public class MessageReadTable {


    public MessageReadTable(){}

    public MessageReadTable( Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Integer state;//消息阅读状态，1-已读，2-未读

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "message_id")
    private MessageTable message;// 关联的消息
}
