package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.model.ProductPriceConfigModel;
import com.cjack.meetingroomadmin.model.ProductPriceTypeModel;
import com.cjack.meetingroomadmin.service.ProductPriceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/productPriceConfig")
public class ProductPriceConfigEndpoint extends BaseEndpoint{

    @Autowired
    ProductPriceConfigService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( ProductPriceConfigModel model) {
        try{
            List<ProductPriceConfigModel> datas = service.list( model);
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
    public AjaxResult addOrUpdate( ProductPriceConfigModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( System.currentTimeMillis());
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
     * 列表
     * @return
     */
    @RequestMapping(value = "/type/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult typeList() {
        try{
            List<ProductPriceTypeModel> datas = service.typeList();
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
    @RequestMapping(value = "/type/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addOrUpdateType( ProductPriceTypeModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( System.currentTimeMillis());
            }
            service.saveType( model);
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
    @RequestMapping(value = "/type/del", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delType( @RequestParam("ids") String ids) {
        try{
            service.delType( ids);
            return AjaxResult.SUCCESS( "删除成功");
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
