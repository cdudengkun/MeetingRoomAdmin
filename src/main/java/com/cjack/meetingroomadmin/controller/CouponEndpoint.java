package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.CouponModel;
import com.cjack.meetingroomadmin.service.CouponService;
import com.cjack.meetingroomadmin.util.CustomerStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 优惠券
 */
@Controller
@RequestMapping("/coupon")
public class CouponEndpoint extends BaseEndpoint{

    @Autowired
    CouponService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, CouponModel model) {
        try{
            service.list( layPage, model);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 新增
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addOrUpdate( CouponModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( System.currentTimeMillis());
                model.setNumberNo( CustomerStringUtil.randomStrOnlyUpperChar( 16));
            }
            model.setStatus( 1);
            model.setContent(CustomerStringUtil.replaceWidth( model.getContent()));
            service.save( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 发布优惠券
     * @return
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult publish( CouponModel model) {
        try{
            service.publish( model);
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
}
