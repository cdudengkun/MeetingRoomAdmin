package com.cjack.meetingroomadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.AppUserDailyLearnInfoModel;
import com.cjack.meetingroomadmin.model.LearnReportModel;
import com.cjack.meetingroomadmin.service.LearnReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/learnReport")
public class LearnReportEndpoint extends BaseEndpoint{

    @Autowired
    LearnReportService learnReportService;

    /**
     * 班级学员学习报告列表
     * @return
     */
    @RequestMapping(value = "/classReport", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult classReport( HttpSession session, LayPage layPage, LearnReportModel model) {
        try{
            Object loginUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            learnReportService.list( layPage, model, (Long)loginUserId);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
    /**
     * 30天学习报告
     * @return
     */
    @RequestMapping(value = "/dailyLearnInfoOfThirty/{appUserId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult dailyLearnInfoOfThirty( @PathVariable("appUserId")Long appUserId) {
        try{
            List<AppUserDailyLearnInfoModel> learnInfoOfDay = learnReportService.dailyLearnInfoOfThirty( appUserId);
            return AjaxResult.SUCCESS( learnInfoOfDay);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 个人学习报告页面需要的数据
     * @return
     */
    @RequestMapping(value = "/studentLearnDetail/{appUserId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult studentLearnDetail( @PathVariable("appUserId")Long appUserId) {
        try{
            JSONObject res = learnReportService.studentLearnDetail( appUserId);
            return AjaxResult.SUCCESS( res);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 个人学习报告下载页面需要的数据
     * @return
     */
    @RequestMapping(value = "/downloadLearnInfo/{appUserId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult downloadLearnInfo( @PathVariable("appUserId")Long appUserId) {
        try{
            JSONObject res = learnReportService.downloadLearnInfo( appUserId);
            return AjaxResult.SUCCESS( res);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
