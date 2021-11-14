package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 共用的只需要id-key的数据项配置表
 */
@Entity
@Table(name="define_sign_key", catalog = "meeting_room")
@Data
public class DefineSignKeyTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dataKey;//数据项名称

    private Integer type;//1-会议室规格，2-企业服务类型,3-合作商城的商品类别,4-会议中心设施
    private Integer sequence;//顺序

    private Long createTime;
    private Long updateTime;
}

