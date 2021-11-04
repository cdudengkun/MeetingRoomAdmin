package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.BatchAddRechargeCardModel;
import com.cjack.meetingroomadmin.model.RechargeCardCount;
import com.cjack.meetingroomadmin.model.RechargeCardModel;
import com.cjack.meetingroomadmin.service.RechargeCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/rechargeCard")
public class RechargeCardEndpoint extends BaseEndpoint{

    @Autowired
    RechargeCardService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, RechargeCardModel model) {
        try{
            service.list( layPage, model);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 批量创建充值卡
     * @return
     */
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult batchAdd( HttpSession session, BatchAddRechargeCardModel model) {
        try{
            Object loginUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setAdminUserId( (Long)loginUserId);
            service.batchAdd( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult del( @RequestParam("ids") String ids) {
        try{
            service.del( ids);
            return AjaxResult.SUCCESS( "删除成功");
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/showCount", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult showCount() {
        try{
            RechargeCardCount model = service.showCount();
            return AjaxResult.SUCCESS( model);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
