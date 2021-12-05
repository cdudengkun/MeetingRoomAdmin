package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 企业服务类型
 */
@Entity
@Table(name="enterprise_service_type", catalog = "meeting_room")
@Data
public class EnterpriseServiceTypeTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String logo;//logo图片地址 未选中时候
    private String logoChosed;//logo图片地址 选中时候
    private String name;//类型名称
    private Integer priority;//展示优先级,越大展示越前面
    private Long adminUserId;//创建人id

    private Integer type;//类型，1-企业服务,2-政策解读,3-礼包领取,4-企业助力

}
