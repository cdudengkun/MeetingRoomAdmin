package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 企业服务
 */
@Entity
@Table(name="enterprise_service", catalog = "meeting_room")
@Data
public class EnterpriseServiceTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String cover;//封面图片地址
    private String title;//消息标题
    private String content;//消息内容
    private Long adminUserId;//创建人id
    private Integer sorting;//展示顺序，越高展示越前面
    private String vedioUrl;//视频文件地址

    @OneToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "type_id")
    private EnterpriseServiceTypeTable type;//企业服务类型
}
