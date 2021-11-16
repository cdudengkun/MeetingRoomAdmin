package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 政策解读表
 */
@Entity
@Table(name="policy_interpretation", catalog = "meeting_room")
@Data
public class PolicyInterpretationTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String cover;//封面
    private String title;//标题
    private Integer type;//内容类型，1-视频章节,2-图文,3-纯图片,4-优惠券
    private String content;//内容
    private Long adminUserId;//创建人id
    private String imgs;//内容3图片列表

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "policyInterpretation" , fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<PolicyInterpretationFileTable> files;//附件文件列表

    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "policyInterpretation" , fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<PolicyInterpretationVideoTable> videos;//视频章节列表

}
