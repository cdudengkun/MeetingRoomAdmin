package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.PolicyInterpretationFileModel;
import com.cjack.meetingroomadmin.model.PolicyInterpretationModel;
import com.cjack.meetingroomadmin.model.PolicyInterpretationVideoModel;
import com.cjack.meetingroomadmin.service.PolicyInterpretationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 政策解读管理
 */
@Controller
@RequestMapping("/policyInterpretation")
public class PolicyInterpretationEndpoint extends BaseEndpoint{

    @Autowired
    PolicyInterpretationService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( HttpSession session, LayPage layPage, PolicyInterpretationModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setAdminUserId( (Long)adminUserId);
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
    public AjaxResult addOrUpdate( HttpSession session, PolicyInterpretationModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( System.currentTimeMillis());
                Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
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
    public AjaxResult uploadImg( PolicyInterpretationModel model) {
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
    public AjaxResult delImg( PolicyInterpretationModel model) {
        try{
            service.delImg( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/video/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult videoList( HttpSession session , LayPage layPage, PolicyInterpretationVideoModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setAdminUserId( (Long)adminUserId);
            service.videoList( layPage, model);
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
    @RequestMapping(value = "/uploadVideoFile", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadVideoFile( PolicyInterpretationVideoModel model) {
        try{
            service.uploadVideoFile( model);
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
    @RequestMapping(value = "/delVideoFile", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delVideoFile( PolicyInterpretationVideoModel model) {
        try{
            service.delVideoFile( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/attachment/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult fileList( HttpSession session , PolicyInterpretationFileModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setAdminUserId( (Long)adminUserId);
            List<PolicyInterpretationFileModel> datas = service.fileList( model);
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
    @RequestMapping(value = "/uploadAttachment", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadAttachment( PolicyInterpretationFileModel model) {
        try{
            service.uploadAttachment( model);
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
    @RequestMapping(value = "/delAttachment", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delAttachment( PolicyInterpretationFileModel model) {
        try{
            service.delAttachment( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
