package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 合作商城
 */
@Entity
@Table(name="cooperation_shopping", catalog = "meeting_room")
@Data
public class CooperationShoppingTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String shop;//商品名称
    private String companyName;//公司名称
    private String name;//联系人
    private String phone;//联系电话
    private String content;//内容
    private String cover;//商品图片

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "type_id")
    private DefineSignKeyTable type;//类别

}
