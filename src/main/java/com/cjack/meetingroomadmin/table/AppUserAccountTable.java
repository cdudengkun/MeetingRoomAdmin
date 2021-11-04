package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

/**
 * app用户账户信息
 */
@Entity
@Table(name="app_user_account")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUserAccountTable {

    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private Double goldCoin;//用户充值的金币余额
    private Integer integral;//用户积分余额

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    private Integer level;//用户的学习级别，从1-15，分别代表1-15级

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Double getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(Double goldCoin) {
        this.goldCoin = goldCoin;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public AppUserTable getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserTable appUser) {
        this.appUser = appUser;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
