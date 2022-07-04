package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.ErrorCodeDefine;
import com.cjack.meetingroomadmin.exception.AdminUserNotFoundException;
import com.cjack.meetingroomadmin.exception.AdminUserPassErrorException;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AdminUserModel;
import com.cjack.meetingroomadmin.model.UpdatePassWordModel;
import com.cjack.meetingroomadmin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *
 * Created by root on 4/21/19.
 */
@Controller
@RequestMapping(value = "/adminUser")
public class AdminUserEndpoint extends BaseEndpoint{

    @Autowired
    AdminUserService service;

    /**
     * 登录
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult login(HttpSession session, String loginName, String passWord) {
        try {
            AdminUserModel model = service.login( loginName, passWord);
            session.setAttribute( CommonConfig.SESSION_NAME, model.getId());
            return AjaxResult.SUCCESS();
        } catch (AdminUserNotFoundException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        } catch (AdminUserPassErrorException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        } catch (CommonException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult logout( HttpSession session) {
        Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
        if( adminUserId == null){
            return AjaxResult.ERROR( ErrorCodeDefine.USER_NOT_LOGIN, ErrorCodeDefine.USER_NOT_LOGIN_MSG);
        }else{
            session.removeAttribute( CommonConfig.SESSION_NAME);
            return AjaxResult.SUCCESS( "登出成功");
        }
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getUserInfo( HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            if( adminUserId == null){
                return AjaxResult.ERROR( ErrorCodeDefine.USER_NOT_LOGIN, ErrorCodeDefine.USER_NOT_LOGIN_MSG);
            }else{
                AdminUserModel model = service.getUserInfo( (Long)adminUserId);
                return AjaxResult.SUCCESS( model);
            }
        }catch (AdminUserNotFoundException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        }
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( HttpSession session, AdminUserModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setAdminUserId( (long)adminUserId);
            return AjaxResult.SUCCESS( service.list(model));
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
    public AjaxResult addOrUpdate( HttpSession session, AdminUserModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            if( isAdd( model.getId())){
                model.setCreateTime( System.currentTimeMillis());
                model.setAdminUserId( (Long)adminUserId);
            }
            model.setPassWord( "123456");
            service.save( model);
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
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updatePassword( UpdatePassWordModel model) {
        try{
            service.updatePassword( model);
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
