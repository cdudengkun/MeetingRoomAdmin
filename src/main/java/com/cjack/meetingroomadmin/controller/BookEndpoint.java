package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.*;
import com.cjack.meetingroomadmin.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookEndpoint extends BaseEndpoint{

    @Autowired
    BookService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, BookModel model) {
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
    public AjaxResult addOrUpdate( BookModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( new Date());
                model.setDownLoadNum( 0);
                model.setWordsNum( 0);
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
    public AjaxResult del( @RequestParam("id") Long id) {
        try{
            service.del( id);
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
    @RequestMapping(value = "/listForDealer", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult listForDealer( HttpSession session, LayPage layPage, BookModel model) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setDealerId( (Long)adminUserId);
            service.list( layPage, model);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 校长名下课本总览信息
     * @return
     */
    @RequestMapping(value = "/listForMasterView", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult listForMasterView( HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            BookForMasterViewModel model = service.listForMasterView( (Long)adminUserId);
            List<BookForMasterViewModel> res = new ArrayList<>();
            res.add( model);
            return AjaxResult.SUCCESS( res);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 校长名下课本最近激活信息
     * @return
     */
    @RequestMapping(value = "/lastSellBookInfo", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult lastSellBookInfo( HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            LastSellBookInfoModel model = service.lastSellBookInfo( (Long)adminUserId);
            return AjaxResult.SUCCESS( model);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 校长名下课本最近激活信息
     * @return
     */
    @RequestMapping(value = "/dealerMonthSellBookInfo", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult dealerMonthSellBookInfo( HttpSession session) {
        try{
            Object adminUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            int res = service.dealerMonthSellBookInfo( (Long)adminUserId);
            return AjaxResult.SUCCESS( res);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 获取系统所有的学习阶段
     * @return
     */
    @RequestMapping(value = "/stage/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult stageList() {
        List<BookStageModel> stages = service.stageList();
        return AjaxResult.SUCCESS( stages);
    }

    /**
     * 获取系统对应阶段下的所有的课本版本
     * @return
     */
    @RequestMapping(value = "/version/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult versionList() {
        List<BookVersionModel> versions = service.versionList();
        return AjaxResult.SUCCESS( versions);
    }

    /**
     * 获取系统对应阶段下的所有的年级
     * @return
     */
    @RequestMapping(value = "/grade/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult gradeList() {
        List<BookGradeModel> grades = service.gradeList();
        return AjaxResult.SUCCESS( grades);
    }
}
