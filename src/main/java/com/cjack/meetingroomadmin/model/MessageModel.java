package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 系统发送给app的消息
 */
@Data
public class MessageModel extends BaseModel{
    private Long id;
    private Long createTime;
    private String title;
    private String content;
    private String img;
    private Integer type;//1-后台手动发送消息，2-系统发送消息

}
