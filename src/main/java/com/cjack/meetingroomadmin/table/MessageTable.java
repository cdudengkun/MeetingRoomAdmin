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
    private Integer type;//1-后台手动发送消息，2-系统发送消息
    private Long adminUserId;//创建人id
}
