package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.SchoolClassModel;
import com.cjack.meetingroomadmin.service.SchoolClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/schoolClass")
public class SchoolClassEndpoint extends BaseEndpoint{

    @Autowired
    SchoolClassService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( HttpSession session, LayPage layPage, SchoolClassModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            service.list( layPage, model, (Long)adminUserId);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 列表，供下拉调用
     * @return
     */
    @RequestMapping(value = "/listForSelect/", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult listForSelect( HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            List<SchoolClassModel> datas = service.list( (Long)adminUserId);
            return AjaxResult.SUCCESS( datas);
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
    public AjaxResult addOrUpdate( HttpSession session, SchoolClassModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( new Date());
                Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
                model.setAdminUserId( (Long)adminUserId);
            }
            model.setUpdateTime( new Date());
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
}
