package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 共用的key-value数据项配置表
 */
@Entity
@Table(name="define_key_value", catalog = "meeting_room")
@Data
public class DefineKeyValueTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dataKey;//数据项名称
    private String dataValue;//数据项值

    private Integer type;//1-会议中心设施服务

    private Long createTime;
    private Long updateTime;
}

