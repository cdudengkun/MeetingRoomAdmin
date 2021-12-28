package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.model.AppConfigModel;
import com.cjack.meetingroomadmin.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 公司，加入我们信息
 */
@Controller
@RequestMapping("/appConfig")
public class AppConfigEndpoint extends BaseEndpoint{

    @Autowired
    AppConfigService service;

    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getConfig() {
        try{
            return AjaxResult.SUCCESS( service.get());
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    @RequestMapping(value = "/saveConfig", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult saveConfig( AppConfigModel model) {
        try{
            service.save( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
