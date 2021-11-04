package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.model.AdminRoleModel;
import com.cjack.meetingroomadmin.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleEndpoint extends BaseEndpoint{

    @Autowired
    AdminRoleService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list() {
        try{
            List<AdminRoleModel> datas = service.list();
            return AjaxResult.SUCCESS( datas);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
