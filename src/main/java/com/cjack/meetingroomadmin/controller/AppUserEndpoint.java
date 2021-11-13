package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AppUserModel;
import com.cjack.meetingroomadmin.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/appUser")
public class AppUserEndpoint extends BaseEndpoint{

    @Autowired
    AppUserService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list(HttpSession session, LayPage layPage, AppUserModel model){
        try{
            Object loginUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            service.list( layPage, model, (Long)loginUserId);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }



    /**
     * 启用禁用
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateStatus( AppUserModel model) {
        try{
            service.updateStatus( model);
            return AjaxResult.SUCCESS();
        }catch ( CommonException e) {
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        }
    }
}
