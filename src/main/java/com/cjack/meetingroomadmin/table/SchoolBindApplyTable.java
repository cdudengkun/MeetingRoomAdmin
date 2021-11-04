package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="school_bind_apply")
@Data
public class SchoolBindApplyTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name="school_id", columnDefinition="bigint(20)")
    private SchoolTable school;

    @ManyToOne
    @JoinColumn(name="app_user_id", columnDefinition="bigint(20)")
    private AppUserTable appUser;

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable adminUser;// 申请同意人
    private Date agreeTime;//同意时间
    private Integer state;//申请状态，1-同意，2-未同意，3-拒绝
}
