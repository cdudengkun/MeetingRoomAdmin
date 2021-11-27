package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.model.EchartDataModel;
import com.cjack.meetingroomadmin.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StatementService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private OrderService orderService;

    //按天统计每天登录的用户，查询最近14天的吧
    public EchartDataModel userLoginCount(){
        EchartDataModel model = new EchartDataModel();
        for( int i = 13; i>= 0; i--){
            Date date = DateFormatUtil.formatLastNDay( i);
            model.addX( DateFormatUtil.format( date, DateFormatUtil.DATE_RULE_8));
            Integer y = appUserService.queryUserLoginCount( date);
            model.addY( y == null ? 0 : y);
        }

        return model;
    }

    //按天统计每天的订单金额，查询最近14天的吧
    public EchartDataModel orderMount(){
        EchartDataModel model = new EchartDataModel();
        for( int i = 13; i>= 0; i--){
            Date date = DateFormatUtil.formatLastNDay( i);
            model.addX( DateFormatUtil.format( date, DateFormatUtil.DATE_RULE_8));
            Integer y = orderService.queryTradeMount( date);
            model.addY( y == null ? 0 : y);
        }
        return model;
    }

}