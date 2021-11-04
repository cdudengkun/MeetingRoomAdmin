package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * app用户阅读系统消息的情况
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MessageReadModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Integer state;//消息阅读状态，1-已读，2-未读
    private AppUserModel appUser;// 所属用户
    private MessageModel message;// 关联的消息
}
