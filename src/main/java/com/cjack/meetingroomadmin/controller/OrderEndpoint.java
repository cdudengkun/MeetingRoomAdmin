package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.AppUserOrderModel;
import com.cjack.meetingroomadmin.model.OrderStatementModel;
import com.cjack.meetingroomadmin.model.OrderStatementQueryModel;
import com.cjack.meetingroomadmin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 订单管理
 */
@Controller
@RequestMapping(value = "/order")
public class OrderEndpoint {

    @Autowired
    OrderService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, AppUserOrderModel model) {
        service.list( layPage, model);
        return AjaxResult.SUCCESS( layPage);
    }

    //修改订单状态
    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateOrderStatus( AppUserOrderModel model) {
        service.updateOrderStatus( model);
        return AjaxResult.SUCCESS();
    }

    //报表数据
    @RequestMapping(value = "/statement", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult statement( OrderStatementQueryModel condition) {
        List<OrderStatementModel> datas = service.statement( condition);
        return AjaxResult.SUCCESS( datas);
    }
}
