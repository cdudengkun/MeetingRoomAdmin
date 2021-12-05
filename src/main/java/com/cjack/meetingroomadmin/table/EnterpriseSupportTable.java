package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 企业助力表
 */
@Entity
@Table(name="enterprise_support", catalog = "meeting_room")
@Data
public class EnterpriseSupportTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String name;//类型名称
    private String cover;//封面图片地址
    private String url;//文件下载地址
    private String size;//文件大小，内容为22MB这种格式
    private Integer downloadCount;//下载次数
    private Long adminUserId;//创建人id

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "type_id")
    private EnterpriseServiceTypeTable type;//企业服务类型

}
