package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 会议室表
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class MeetingRoomModel extends BaseModel{

    private String cover;
    private Integer capicity;
    private Integer price;
    private String addrDetail;
    private String levelName;//规格

    private MeetingZoneModel meetingZoneModel;
    private Long meetingZoneId;
    private Long levelId;

    private Integer status;//状态，0-未审核，1-审核通过，2-审核拒绝
}
