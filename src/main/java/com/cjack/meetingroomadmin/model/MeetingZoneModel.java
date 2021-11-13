package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.List;

/**
 * 会议中心表
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class MeetingZoneModel  extends BaseModel{

    public MeetingZoneModel(){}


    private Long provinceId;
    private String provinceName = "";
    private Long cityId;
    private String cityName = "";
    private Long countryId;
    private String countryName = "";
    private String detail;
    private List<String> facilitys;
    private String cover;
    private String lnt;
    private String lat;
    private String availableDayTime;
    private Integer availableWeekDay;
    private String imgs;


}
