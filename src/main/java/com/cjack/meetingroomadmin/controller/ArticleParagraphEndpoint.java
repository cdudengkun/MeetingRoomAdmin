package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.model.ArticleParagraphModel;
import com.cjack.meetingroomadmin.service.ArticleParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/articleParagraph")
public class ArticleParagraphEndpoint extends BaseEndpoint{

    @Autowired
    ArticleParagraphService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list/{articleId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( @PathVariable( "articleId")Long articleId) {
        try{
            List<ArticleParagraphModel> datas = service.list( articleId);
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
    public AjaxResult addOrUpdate( ArticleParagraphModel model) {
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
