package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name="policy_interpretation_file", catalog = "meeting_room")
@Data
public class PolicyInterpretationFileTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String name;//类型名称
    private String url;//文件下载地址
    private String size;//文件大小，内容为22MB这种格式
    private Integer downloadCount;//下载次数

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "policy_interpretation_id")
    private PolicyInterpretationTable policyInterpretation;

}
