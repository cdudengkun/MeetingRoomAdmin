package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 后端用户角色列表
 */
@Entity
@Table(name="admin_role", catalog = "meeting_room")
@Data
public class AdminRoleTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String code;//角色编码
    private String roleName;//角色名称
    private String content;//角色权限内容
    private Long adminUserId;//创建人id
}
