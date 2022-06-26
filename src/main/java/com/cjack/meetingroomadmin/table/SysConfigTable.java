package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="sys_config", catalog = "meeting_room")
@Data
public class SysConfigTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    private String memberRightTitle;

    private String memberRight;

    private String textEnterpriseService;
    private String textPolicyInterpretation;
    private String textGift;
    private String textEnterpriseSupport;

    private String privacyPolicy;//隐私政策

}
