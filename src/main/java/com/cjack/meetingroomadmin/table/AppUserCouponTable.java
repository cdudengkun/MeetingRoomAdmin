package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户优惠券
 */
@Entity
@Table(name="app_user_coupon", catalog = "meeting_room")
@Data
public class AppUserCouponTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private Long endTime;//过期时间
    private Long useTime;//优惠券使用时间
    private Integer status;//优惠券状态，1-已使用,2-未使用

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "coupon_id")
    private CouponTable coupon;//关联优惠券


    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 优惠券所属用户id

}
