package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.List;

/**
 * 政策解读表
 */
@Data
public class PolicyInterpretationModel extends BaseModel{

    private String cover;//封面
    private String title;//标题
    private Integer type;//内容类型，1-视频章节,2-图文,3-纯图片,4-优惠券
    private String content;//内容

    private String imgs;//内容3图片列表

    private String img;

    private List<PolicyInterpretationFileModel> files;//附件文件列表

    private List<PolicyInterpretationVideoModel> videos;//视频章节列表

}
