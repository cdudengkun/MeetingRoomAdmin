package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 充值卡
 */
@Entity
@Table(name="recharge_card")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class RechargeCardTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Date activeTime;//充值卡激活时间
    private Double amount;//充值卡对应金额
    private String code;//充值卡卡号
    private String description;//充值卡描述
    private Integer validityDay;//有效期（天）

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "admin_user_id")
    private AdminUserTable adminUser;//充值卡创建人

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 充值卡激活App用户
    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "school_id")
    private SchoolTable school;//充值卡所属学校id

    private Integer status;//充值卡状态，1-已使用，2-未使用
}
