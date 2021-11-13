package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 系统发送给app的消息
 */
@Entity
@Table(name="message", catalog = "meeting_room")
@Data
public class MessageTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String title;//消息标题
    private String content;//消息内容
}
