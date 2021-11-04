package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.model.CityModel;
import com.cjack.meetingroomadmin.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 全国省市县信息
 * Created by root on 4/21/19.
 */
@Controller
@RequestMapping("/city")
public class CityEndpoint extends BaseEndpoint{

    @Autowired
    CityService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( @PathVariable("parentId")Long parentId) {

        try{
            //注意pageDate里面的页数是从0开始计算的
            List<CityModel> datas = service.list( parentId);
            return AjaxResult.SUCCESS( datas);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
