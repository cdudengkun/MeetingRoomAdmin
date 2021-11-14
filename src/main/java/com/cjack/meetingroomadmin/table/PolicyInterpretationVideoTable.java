package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 政策解读表视频表
 */
@Entity
@Table(name="enterprise_support_video", catalog = "meeting_room")
@Data
public class PolicyInterpretationVideoTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String title;//标题
    private String url;//视频文件地址
    private String size;//文件大小，内容为22MB这种格式
    private Integer visitCount;//观看次数
    private Long adminUserId;//创建人id

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "policy_interpretation_id")
    private PolicyInterpretationTable policyInterpretation;

}
