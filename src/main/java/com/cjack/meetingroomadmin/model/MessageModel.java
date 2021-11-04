package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 系统发送给app的消息
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MessageModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String title;//消息标题
    private String content;//消息内容
    private Integer type;//1-系统消息，2-学校消息，3-班级消息
    //消息类型为学校消息时，对应的学校id
    private Long schoolId;
    private String schoolName;
    //消息类型为班级消息时，对应的班级id
    private Long schoolClassId;
    private String schoolClassName;
    //发送人
    private Long adminUserId;
    private String adminUserName;
}
