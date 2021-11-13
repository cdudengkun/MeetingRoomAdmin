package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户订单表
 */
@Entity
@Table(name="app_user_order", catalog = "meeting_room")
@Data
public class AppUserOrderTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;


    private Integer amount;//订单金额
    private String orderNo;//订单编号
    private String memo;//订单说明
    private Integer payType;//支付方式，这里默认全部都是 1-微信支付
    private Long payTime;//支付时间
    private Integer status;//状态，1-待付款，2-进行中，3-已完成，4-已取消
    private Integer vipHour;//如果是会议室，则是订单使用会员权益时长；如果是工位，则是会员权益天数

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_coupon_id")
    private AppUserCouponTable appUserCoupon;//使用优惠券

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private AppUserOrderDetailTable detail;//关联订单详细，这里现在订单和订单明细是一对一

}
