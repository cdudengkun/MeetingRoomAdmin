package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * app用户阅读系统消息的情况
 */
@Data
public class MessageReadModel extends BaseModel{
    private Integer state;//消息阅读状态，1-已读，2-未读

    private AppUserModel appUser;// 所属用户

    private MessageModel message;// 关联的消息
}
