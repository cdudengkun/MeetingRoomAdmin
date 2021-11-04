package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.model.SchoolModel;
import com.cjack.meetingroomadmin.service.SchoolService;
import com.cjack.meetingroomadmin.util.CustomerStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/school")
public class SchoolEndpoint extends BaseEndpoint{

    @Autowired
    SchoolService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( SchoolModel model) {
        try{

            List<SchoolModel> datas = service.list( model);
            return AjaxResult.SUCCESS( datas);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 添加校长加载学校列表，返回的数据需要排除已经绑定校长的学校
     * @return
     */
    @RequestMapping(value = "/listForAddMaster", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult listForAddMaster( Long editUserId) {
        try{
            List<SchoolModel> datas = service.listForAddMaster( editUserId);
            return AjaxResult.SUCCESS( datas);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 添加教师加载学校列表
     * @return
     */
    @RequestMapping(value = "/listForAddTeacher", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult listForAddTeacher( HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            List<SchoolModel> datas = service.listForAddTeacher( (Long)adminUserId);
            return AjaxResult.SUCCESS( datas);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/listForDealer", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult listForDealer( HttpSession session, SchoolModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            List<SchoolModel> datas = service.listForDealer( (Long)adminUserId, model);
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
    public AjaxResult addOrUpdate( SchoolModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( new Date());
                model.setUid( CustomerStringUtil.randomNumberStr( 7));
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
