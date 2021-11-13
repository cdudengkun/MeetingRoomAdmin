package com.cjack.meetingroomadmin.model;

import lombok.Data;

/**
 * 政策解读表视频表
 */
@Data
public class PolicyInterpretationVideoModel  extends BaseModel{


    private String title;//标题
    private String url;//视频文件地址
    private String size;//文件大小，内容为22MB这种格式
    private Integer visitCount;//观看次数

    private PolicyInterpretationModel policyInterpretationTable;

}
