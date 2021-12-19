package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * app用户信息
 */
@Entity
@Table(name="app_user_login", catalog = "meeting_room")
@Data
public class AppUserLoginTable {

    public AppUserLoginTable(){

    }
    public AppUserLoginTable(Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY)
    private Long id;
    private Long createTime;
    private Long updateTime;
    private Long appUserId;
    private Long loginTime;//登录时间

}
