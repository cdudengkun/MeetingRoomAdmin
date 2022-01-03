package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * Created by root on 5/26/19.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class AppConfigModel  extends BaseModel{

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
