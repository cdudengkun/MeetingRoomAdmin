package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统发送给app的消息
 */
@Entity
@Table(name="message")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MessageTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String title;//消息标题
    private String content;//消息内容
    private Integer type;//1-系统消息，2-学校消息，3-班级消息

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "school_id")
    private SchoolTable school;//消息类型为学校消息时，对应的学校id

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "class_id")
    private SchoolClassTable schoolClass;//消息类型为班级消息时，对应的班级id

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable adminUser;//发送人
}
