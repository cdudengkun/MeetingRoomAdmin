package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.CompanyJoinInfoModel;
import com.cjack.meetingroomadmin.service.CompanyJoinInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/**
 * 公司，加入我们信息
 */
@Controller
@RequestMapping("/companyJoinInfo")
public class CompanyJoinInfoEndpoint extends BaseEndpoint{

    @Autowired
    CompanyJoinInfoService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, CompanyJoinInfoModel model) {
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
    public AjaxResult addOrUpdate(HttpSession session, CompanyJoinInfoModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            if( isAdd( model.getId())){
                model.setCreateTime( System.currentTimeMillis());
                model.setAdminUserId( (Long)adminUserId);
            }
            service.save( model);
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
     * 新增
     * @return
     */
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadImg( CompanyJoinInfoModel model) {
        try{
            service.uploadImg( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 新增
     * @return
     */
    @RequestMapping(value = "/delImg", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delImg( CompanyJoinInfoModel model) {
        try{
            service.delImg( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
