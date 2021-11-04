package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 后端用户角色列表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AdminRoleModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String code;//角色编码
    private String roleName;//角色名称
    private String content;//角色权限内容
}
