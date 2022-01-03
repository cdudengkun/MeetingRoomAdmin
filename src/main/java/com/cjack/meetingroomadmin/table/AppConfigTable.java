package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * id must is 1
 * Created by root on 5/26/19.
 */
@Entity
@Table(name="app_config", catalog = "meeting_room")
@Data
public class AppConfigTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;

    private Integer menmberPrice1;
    private Integer menmberPrice3;
    private Integer menmberPrice12;
    private String memberRight;
    private String memberWelfare;//会员福利，多个英文逗号分割

    private String textEnterpriseService;
    private String textPolicyInterpretation;
    private String textGift;
    private String textEnterpriseSupport;



}
