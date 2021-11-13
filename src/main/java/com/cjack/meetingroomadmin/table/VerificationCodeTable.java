package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 验证码表
 */
@Entity
@Table(name="verification_code", catalog = "meeting_room")
@Data
public class VerificationCodeTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Long expireTime;//验证码过期时间-单位毫秒
    private String phone;//电话
    private String code;//验证码
    private Integer type;//验证码类型 1-注册,2-修改密码,3-绑定手机
    private Integer state;//状态 1-已用，2-未用

}
