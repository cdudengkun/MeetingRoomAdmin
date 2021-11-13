package com.cjack.meetingroomadmin.model;

import lombok.Data;


/**
 * Created by root on 5/26/19.
 */
@Data
public class AdvertisementModel extends BaseModel{

    private String imgUrl;
    private String url;
    private String sequence;
    private String title;
    private Integer type;
}
