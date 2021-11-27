package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Echart图表data
 */
@Data
public class EchartDataModel extends BaseModel{
    private List<String> xSeries = new ArrayList<>();
    private List<Integer> ySeries = new ArrayList<>();

    public void addX( String x){
        xSeries.add( x);
    }

    public void addY( Integer y){
        ySeries.add( y);
    }

}
