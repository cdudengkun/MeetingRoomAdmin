package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 咨询信息表
 */
@Entity
@Table(name="consult_info", catalog = "meeting_room")
@Data
public class ConsultInfoTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String title;//问题标题
    private String content;//解答内容
    private String keyWords;//关键字，多个关键字英文逗号分割
    private Integer isHot;//是否热门，1-是，2-否
    private Integer matchCount;//匹配次数
    private Long adminUserId;//创建人id
}
