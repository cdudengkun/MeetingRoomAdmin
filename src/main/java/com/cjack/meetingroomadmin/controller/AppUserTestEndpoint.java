package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.model.LearnUnitCommentModel;
import com.cjack.meetingroomadmin.model.SeachBookTestModel;
import com.cjack.meetingroomadmin.model.TestUnitModel;
import com.cjack.meetingroomadmin.service.TestBookService;
import com.cjack.meetingroomadmin.service.TestUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/appUserTest")
public class AppUserTestEndpoint extends BaseEndpoint{

    @Autowired
    TestBookService testBookService;
    @Autowired
    TestUnitService testUnitService;

    /**
     * 课本测试信息列表
     * @return
     */
    @RequestMapping(value = "/bookTests", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult bookTests( HttpSession session, LayPage layPage, SeachBookTestModel model) {
        try{
            Object loginUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            testBookService.list( layPage, model, (Long)loginUserId);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
    /**
     * 单元测试信息列表
     * @return
     */
    @RequestMapping(value = "/unitTests/{learnBookId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult unitTests( @PathVariable("learnBookId")Long learnBookId) {
        try{
            List<TestUnitModel> testUnits = testUnitService.list( learnBookId);
            return AjaxResult.SUCCESS( testUnits);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 添加单元点评
     * @return
     */
    @RequestMapping(value = "/addComent", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addComent( HttpSession session, LearnUnitCommentModel model) {
        try{
            Object loginUserId = session.getAttribute( CommonConfig.SESSION_NAME);
            model.setAdminUserId( (Long)loginUserId);
            testUnitService.addComment( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
