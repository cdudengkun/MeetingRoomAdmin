package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AppUserModel;
import com.cjack.meetingroomadmin.model.BatchAddAppUserModel;
import com.cjack.meetingroomadmin.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

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
     * 新增
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addOrUpdate( AppUserModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( new Date());
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
     * 分配学员到班级
     * @return
     */
    @RequestMapping(value = "/assignClass", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult assignClass( AppUserModel model) {
        try{
            service.save( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 校长通过uid添加学员
     * @return
     */
    @RequestMapping(value = "/assignSchool", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult assignSchool( String uid, HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            service.assignSchool( uid, (Long)adminUserId);
            return AjaxResult.SUCCESS();
        }catch ( CommonException e) {
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        }
    }

    /**
     * 从学校中删除学员
     * @return
     */
    @RequestMapping(value = "/delFromSchool", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delFromSchool( AppUserModel model) {
        try{
            service.delFromSchool( model);
            return AjaxResult.SUCCESS( "删除成功");
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 从班级中删除学员
     * @return
     */
    @RequestMapping(value = "/delFromClass", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delFromClass( AppUserModel model) {
        try{
            service.delFromClass( model);
            return AjaxResult.SUCCESS( "删除成功");
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 批量生成学员
     * @return
     */
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult batchAdd( BatchAddAppUserModel model) {
        try{
            service.batchAdd( model);
            return AjaxResult.SUCCESS( "删除成功");
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 批量生成学员
     * @return
     */
    @RequestMapping(value = "/batchExportUid", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult batchExportUid( @RequestParam("ids")String ids) {
        try{
            String fileUrl = service.batchExportUid( ids);
            return AjaxResult.SUCCESS( (Object)fileUrl);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
