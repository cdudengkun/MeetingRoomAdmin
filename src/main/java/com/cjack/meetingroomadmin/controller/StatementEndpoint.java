package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 报表相关
 */
@Controller
@RequestMapping("/statement")
public class StatementEndpoint extends BaseEndpoint{

    @Autowired
    StatementService service;

    /**
     * 系统用户量图表数据
     * @return
     */
    @RequestMapping(value = "/userLoginCount", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult userLoginCount() {
        try{
            return AjaxResult.SUCCESS( service.userLoginCount());
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 系统订单图表数据
     * @return
     */
    @RequestMapping(value = "/orderMount", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult orderMount() {
        try{
            return AjaxResult.SUCCESS( service.orderMount());
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
