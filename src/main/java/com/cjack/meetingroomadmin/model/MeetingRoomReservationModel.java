package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

/**
 * 会议室预定信息表
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class MeetingRoomReservationModel  extends BaseModel{

    private Long startTime;
    private Long endTime;
    private String name;
    private String phone;
    private Integer hour;
}
